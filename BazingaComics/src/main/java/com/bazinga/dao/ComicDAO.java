package com.bazinga.dao;

import com.bazinga.model.Comic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComicDAO {

    public List<Comic> getAllComics() {
        List<Comic> comics = new ArrayList<>();
        String sql = "SELECT id, title, author, price FROM comics";
        try (Connection conn = ConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Comic c = new Comic(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price")
                );
                comics.add(c);
            }
            return comics;
        } catch (SQLException e) {
            // If DB not available, return demo data to allow running without Oracle
            System.err.println("DB error (using demo data): " + e.getMessage());
        }

        // demo data
        comics.add(new Comic(1L, "Spider-Man: Homecoming", "Stan Lee", 199.0));
        comics.add(new Comic(2L, "Batman: Year One", "Frank Miller", 249.0));
        comics.add(new Comic(3L, "Watchmen", "Alan Moore", 299.0));
        return comics;
    }

    public Comic findById(Long id) {
        // simple demo implementation (no DB query)
        return getAllComics().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }
}
