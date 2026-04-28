package dao;

import modelo.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorDAO {
    String url = "jdbc:mysql://localhost:3306/productora_cine";
    String user = "root";
    String pass = "1234";

    public int insertarActor(Actor a) {
        String sql = "insert into actores (nombre, nacionalidad, edad) values (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, a.getNombre());
            pstmt.setString(2, a.getNacionalidad());
            pstmt.setInt(3, a.getEdad());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void actualizarActor(Actor a, int id) {
        String sql = "update actores set nombre=?, nacionalidad=?, edad=? where id=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getNombre());
            pstmt.setString(2, a.getNacionalidad());
            pstmt.setInt(3, a.getEdad());
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void borrarActor(int id) {
        String sql = "delete from actores where id=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void asignarActorAPelicula(int actorId, int peliculaId, String personaje) {
        String sql = "insert into reparto (actor_id, pelicula_id, personaje) values (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, actorId);
            pstmt.setInt(2, peliculaId);
            pstmt.setString(3, personaje);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarActorDePelicula(int actorId, int peliculaId) {
        String sql = "delete from reparto where actor_id=? and pelicula_id=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, actorId);
            pstmt.setInt(2, peliculaId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Map<String, Object>> getNumeroActoresPorNacionalidad() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select nacionalidad, count(id) as total_actores from actores group by nacionalidad";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nacionalidad", rs.getString("nacionalidad"));
                fila.put("total_actores", rs.getInt("total_actores"));
                list.add(fila);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public double getEdadMediaActores() {
        double media = 0;
        String sql = "select avg(edad) as edad_media from actores";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                media = rs.getDouble("edad_media");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return media;
    }

    public List<Actor> getActoresSinPelicula() {
        List<Actor> list = new ArrayList<>();
        String sql = "select * from actores where id not in (select actor_id from reparto)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Actor a = new Actor();
                a.setId(rs.getInt("id"));
                a.setNombre(rs.getString("nombre"));
                a.setNacionalidad(rs.getString("nacionalidad"));
                a.setEdad(rs.getInt("edad"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
}
