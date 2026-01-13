package site.sunmeat.contactscontract


import android.content.ContentUris
import android.content.Context
import android.provider.ContactsContract

object ContactsRepository {

    /**
     * Возвращает людей, у которых ДР в декабре.
     * Требует permission: READ_CONTACTS
     */
    fun getDecemberBirthdays(context: Context): List<BirthdayPerson> {
        val res = mutableListOf<BirthdayPerson>()

        val cr = context.contentResolver

        // 1) берём все события дней рождения из Data
        val uri = ContactsContract.Data.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Data.CONTACT_ID,                    // id контакта
            ContactsContract.Data.DISPLAY_NAME,                  // имя
            ContactsContract.CommonDataKinds.Event.START_DATE,   // дата
            ContactsContract.CommonDataKinds.Event.TYPE          // тип события (birthday)
        )

        val selection =
            "${ContactsContract.Data.MIMETYPE}=? AND " +
                    "${ContactsContract.CommonDataKinds.Event.TYPE}=?"

        val selectionArgs = arrayOf(
            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY.toString()
        )

        val cursor = cr.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use { c ->
            val idxId = c.getColumnIndexOrThrow(ContactsContract.Data.CONTACT_ID)
            val idxName = c.getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME)
            val idxDate = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Event.START_DATE)

            while (c.moveToNext()) {
                val contactId = c.getLong(idxId)
                val name = c.getString(idxName) ?: continue
                val birthdayRaw = c.getString(idxDate) ?: continue

                // Парсим месяц/день из разных форматов
                // форматы могут быть: "1999-12-20" или "--12-20" или "20.12.1999"
                val dm = extractDayMonth(birthdayRaw) ?: continue
                val month = dm.first
                val day = dm.second

                // берём только декабрь
                if (month != 12) continue

                val pretty = String.format("%02d.%02d", day, month)

                res.add(
                    BirthdayPerson(
                        contactId = contactId,
                        name = name,
                        birthdayRaw = birthdayRaw,
                        birthdayPretty = pretty
                    )
                )
            }
        }

        return res
    }

    /**
     * Вытягивает телефон контакта по contactId (если нужно для SMS).
     * Требует permission: READ_CONTACTS
     */
    fun getFirstPhoneForContact(context: Context, contactId: Long): String? {
        val cr = context.contentResolver

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val selection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID}=?"
        val selectionArgs = arrayOf(contactId.toString())

        val cursor = cr.query(uri, projection, selection, selectionArgs, null)
        cursor?.use { c ->
            if (c.moveToFirst()) {
                return c.getString(0)
            }
        }
        return null
    }

    /**
     * Парсит (month, day) из:
     * "1999-12-20" -> (12,20)
     * "--12-20"    -> (12,20)
     * "20.12.1999" -> (12,20)
     */
    private fun extractDayMonth(dateRaw: String): Pair<Int, Int>? {
        val s = dateRaw.trim()

        // ISO: 1999-12-20
        if (s.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
            val parts = s.split("-")
            val month = parts[1].toIntOrNull() ?: return null
            val day = parts[2].toIntOrNull() ?: return null
            return month to day
        }

        // Google format: --12-20
        if (s.matches(Regex("--\\d{2}-\\d{2}"))) {
            val parts = s.split("-")
            val month = parts[1].toIntOrNull() ?: return null
            val day = parts[2].toIntOrNull() ?: return null
            return month to day
        }

        // UA/RU: 20.12.1999 или 20.12
        if (s.matches(Regex("\\d{1,2}\\.\\d{1,2}(\\.\\d{2,4})?"))) {
            val parts = s.split(".")
            val day = parts[0].toIntOrNull() ?: return null
            val month = parts[1].toIntOrNull() ?: return null
            return month to day
        }

        return null
    }
}
