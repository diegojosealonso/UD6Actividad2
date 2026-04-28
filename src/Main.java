import dao.ActorDAO;
import dao.PeliculaDAO;
import modelo.Actor;
import modelo.Pelicula;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        PeliculaDAO peliculaDAO = new PeliculaDAO();
        ActorDAO actorDAO = new ActorDAO();


        Pelicula p = new Pelicula();
        p.setTitulo("Terry 2: Más Terry que Nunca");
        p.setGenero("Thriller");
        p.setDuracion(127);
        p.setPresupuesto(11000000);

        int idPelicula = peliculaDAO.insertarPelicula(p);
        System.out.println("Película insertada correctamente");

        p.setTitulo("Terry 2: Capi Contraataca");
        peliculaDAO.actualizarPelicula(p, idPelicula);
        System.out.println("Película actualizada correctamente");

        peliculaDAO.borrarPelicula(idPelicula);
        System.out.println("Película borrada");

        System.out.println("Películas y total de actores: ");
        for (Map<String, Object> fila : peliculaDAO.getPeliculasYTotalActores()) {
            System.out.println(fila.get("titulo") + " " + fila.get("total_actores"));
        }

        System.out.println("Actores de la película 1: ");
        for (Actor a : peliculaDAO.getActoresSegunPelicula(1)) {
            System.out.println(a.getNombre());
        }

        System.out.println("Películas con más de 3 actores: ");
        for (Map<String, Object> fila : peliculaDAO.getPeliculasConMasDe3Actores()) {
            System.out.println(fila.get("titulo") + " " + fila.get("total_actores"));
        }

        System.out.println("Top 3 películas por presupuesto: ");
        for (Pelicula pelicula : peliculaDAO.getTop3PeliculasMayorPresupuesto()) {
            System.out.println(pelicula.getTitulo() + " " + pelicula.getPresupuesto());
        }

        System.out.println("Película más larga de Acción: ");
        Pelicula masLarga = peliculaDAO.getPeliculaMasLargaPorGenero("Acción");
        System.out.println(masLarga.getTitulo() + " " + masLarga.getDuracion() + " minutos");



        Actor a = new Actor();
        a.setNombre("Alfonso de Terry");
        a.setNacionalidad("España");
        a.setEdad(20);

        int idActor = actorDAO.insertarActor(a);
        System.out.println("Actor insertado correctamente");

        a.setNombre("Terry de Alfonso");
        actorDAO.actualizarActor(a, idActor);
        System.out.println("Actor actualizado  correctamente");

        actorDAO.borrarActor(idActor);
        System.out.println("Actor borrado  correctamente");

        actorDAO.asignarActorAPelicula(1, 2, "Terry Bond");
        System.out.println("Actor asignado  correctamente");

        actorDAO.eliminarActorDePelicula(1, 2);
        System.out.println("Actor eliminado correctamente");

        System.out.println("Actores por nacionalidad: ");
        for (Map<String, Object> fila : actorDAO.getNumeroActoresPorNacionalidad()) {
            System.out.println(fila.get("nacionalidad") + " " + fila.get("total_actores"));
        }

        System.out.println("Edad media de los actores: ");
        System.out.println(actorDAO.getEdadMediaActores());

        System.out.println("Actores sin película: ");
        List<Actor> sinPelicula = actorDAO.getActoresSinPelicula();
        if (sinPelicula.isEmpty()) {
            System.out.println("Todos los actores tienen película");
        } else {
            for (Actor actor : sinPelicula) {
                System.out.println(actor.getNombre());
            }
        }
    }
}