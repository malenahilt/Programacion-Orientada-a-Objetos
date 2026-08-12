import java.util.ArrayList;

// =========================================================
// VERSIÓN "incorrecta" 
// =========================================================
// calcularArea() no está declarado acá.
// Cada subclase lo define por su cuenta, sin ningún vínculo
// con la clase base.
class ElementoGrafico {
    protected int x;
    protected int y;
    protected String color;

    public ElementoGrafico(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void moverA(int nuevoX, int nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
    }

    public void cambiarColor(String nuevoColor) {
        this.color = nuevoColor;
    }

    //no hay calcularArea().
}

// =========================================================
// SUBCLASES
// =========================================================
class Rectangulo extends ElementoGrafico {
    protected double base;
    protected double altura;

    public Rectangulo(int x, int y, String color, double base, double altura) {
        super(x, y, color);
        this.base = base;
        this.altura = altura;
    }

    // calcularArea() solo existe acá, en Rectangulo.
    // Java no sabe nada de esto cuando trabaja con el tipo ElementoGrafico.
    public double calcularArea() {
        return base * altura;
    }
}

class Elipse extends ElementoGrafico {
    protected double semiEjeMayor;
    protected double semiEjeMenor;

    public Elipse(int x, int y, String color, double semiEjeMayor, double semiEjeMenor) {
        super(x, y, color);
        this.semiEjeMayor = semiEjeMayor;
        this.semiEjeMenor = semiEjeMenor;
    }

    // calcularArea() solo existe acá, en Elipse.
    public double calcularArea() {
        return Math.PI * semiEjeMayor * semiEjeMenor;
    }
}

// =========================================================
// EL "MOTOR DE RENDERIZADO"
// =========================================================
class Lienzo {
    private ArrayList<ElementoGrafico> elementos = new ArrayList<>();

    public void agregarElemento(ElementoGrafico elemento) {
        elementos.add(elemento);
    }

    public void renderizar() {
        double areaTotal = 0;

        for (ElementoGrafico elemento : elementos) {
            elemento.cambiarColor("#808080");   // esto sí compila (está en la base)
            elemento.moverA(0, 0);              // esto sí compila (está en la base)

            // *** ACÁ ESTÁ EL ERROR DE COMPILACIÓN ***
            // "elemento" es de tipo ElementoGrafico, y esa clase
            // no tiene un método calcularArea(). No importa que el
            // objeto real adentro sea un Rectangulo o una Elipse:
            // el compilador solo mira el tipo declarado de la variable.
            areaTotal += elemento.calcularArea();
        }

        System.out.println("Área total ocupada: " + areaTotal + " píxeles");
    }
}

// =========================================================
// PROGRAMA PRINCIPAL
// =========================================================
public class tp5_ej4_version2 {
    public static void main(String[] args) {
        Lienzo lienzo = new Lienzo();

        lienzo.agregarElemento(new Rectangulo(10, 20, "#FF0000", 40, 20));
        lienzo.agregarElemento(new Elipse(15, 25, "#00FF00", 30, 15));

        lienzo.renderizar();
    }
}
