package dao;

import modelo.Actor;
import modelo.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeliculaDAO {
    String url = "jdbc:mysql://localhost:3306/productora_cine";
    String user = "root";
    String pass = "1234";

    public int insertarPelicula(Pelicula p) {
        String sql = "insert into peliculas (titulo, genero, duracion, presupuesto) values (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, p.getTitulo());
            pstmt.setString(2, p.getGenero());
            pstmt.setInt(3, p.getDuracion());
            pstmt.setDouble(4, p.getPresupuesto());
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

    public void actualizarPelicula(Pelicula p, int id){
        String sql = "update peliculas set titulo=?, genero=?, duracion=?, presupuesto=? where id=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getTitulo());
            pstmt.setString(2, p.getGenero());
            pstmt.setInt(3, p.getDuracion());
            pstmt.setDouble(4, p.getPresupuesto());
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void borrarPelicula (int id){
        String sql = "delete from peliculas where id=?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Map<String, Object>> getPeliculasYTotalActores() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select p.titulo, count(r.actor_id) as total_actores from peliculas p left join reparto r on p.id = r.pelicula_id group by p.id, p.titulo";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("titulo", rs.getString("titulo"));
                fila.put("total_actores", rs.getInt("total_actores"));
                list.add(fila);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Actor> getActoresSegunPelicula(int peliculaId) {
        List<Actor> list = new ArrayList<>();
        String sql = "select a.* from actores a inner join reparto r on a.id = r.actor_id where r.pelicula_id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, peliculaId);
            ResultSet rs = ps.executeQuery();
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

    public List<Map<String, Object>> getPeliculasConMasDe3Actores() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select p.titulo, count(r.actor_id) as total_actores from peliculas p inner join reparto r on p.id = r.pelicula_id group by p.id, p.titulo having count(r.actor_id) > 3";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("titulo", rs.getString("titulo"));
                fila.put("total_actores", rs.getInt("total_actores"));
                list.add(fila);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Pelicula> getTop3PeliculasMayorPresupuesto() {
        List<Pelicula> list = new ArrayList<>();
        String sql = "select * from peliculas order by presupuesto desc limit 3";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelicula p = new Pelicula();
                p.setId(rs.getInt("id"));
                p.setTitulo(rs.getString("titulo"));
                p.setGenero(rs.getString("genero"));
                p.setDuracion(rs.getInt("duracion"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public Pelicula getPeliculaMasLargaPorGenero(String genero) {
        Pelicula p = null;
        String sql = "select * from peliculas where genero = ? order by duracion desc limit 1";
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, genero);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                p = new Pelicula();
                p.setId(rs.getInt("id"));
                p.setTitulo(rs.getString("titulo"));
                p.setGenero(rs.getString("genero"));
                p.setDuracion(rs.getInt("duracion"));
                p.setPresupuesto(rs.getDouble("presupuesto"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return p;
    }
}
