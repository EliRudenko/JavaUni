package site.sunmeat.contactscontract

data class BirthdayPerson(
    val contactId: Long,
    val name: String,
    val birthdayRaw: String,      // например "2001-12-24" или "24.12.2001"
    val birthdayPretty: String    // например "24.12"
)
