import java.util.ArrayList;

// =========================================================
// CLASE BASE (ABSTRACTA)
// =========================================================
// Se la define como clase abstracta
abstract class ElementoGrafico {
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

    // *** EL CONTRATO ***
    // Ambos métodos son abstractos: cualquier clase futura que
    // herede de ElementoGrafico (Triangulo, Pentagono, lo que sea)
    // está obligada a implementarlo.
    //  Así se garantiza que el bucle del
    // Lienzo pueda llamarlos de forma polimórfica sobre cualquier
    // elemento, sin importar la figura concreta.
    public abstract double calcularArea();
    public abstract double calcularPerimetro();

    @Override
    public String toString() {
        return String.format("%s [x=%d, y=%d, color=%s, area=%.2f, perimetro=%.2f]",
                getClass().getSimpleName(), x, y, color, calcularArea(), calcularPerimetro());
    }
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

    @Override
    public double calcularArea() {
        return base * altura;
    }

    @Override
    public double calcularPerimetro() {
        return 2 * (base + altura);
    }
}

// Un Cuadrado es-un Rectángulo con base == altura (reutilizamos código,
// incluido calcularPerimetro() heredado: 2*(lado+lado) = 4*lado)
class Cuadrado extends Rectangulo {
    public Cuadrado(int x, int y, String color, double lado) {
        super(x, y, color, lado, lado);
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

    @Override
    public double calcularArea() {
        return Math.PI * semiEjeMayor * semiEjeMenor;
    }

    @Override
    public double calcularPerimetro() {
        double a = semiEjeMayor;
        double b = semiEjeMenor;
        return Math.PI * (3 * (a + b) - Math.sqrt((3 * a + b) * (a + 3 * b)));
    }
}

// Un Círculo es-una Elipse con ambos semiejes iguales (el radio).
// Nota: calcularPerimetro() heredado, con a=b=radio, la fórmula de
// Ramanujan se simplifica exactamente a 2*pi*radio -> coincide con
// la fórmula clásica de la circunferencia.
class Circulo extends Elipse {
    public Circulo(int x, int y, String color, double radio) {
        super(x, y, color, radio, radio);
    }
}

// Clase "futura" del enunciado: se agrega despues y, gracias al
// contrato de ElementoGrafico, queda obligada a implementar ambos
// métodos. Si te olvidás de alguno, el programa directamente no
// compila (prueba de que la abstracción cumple su función).
class Triangulo extends ElementoGrafico {
    protected double ladoA;
    protected double ladoB;
    protected double ladoC;
    protected double base;
    protected double altura;

    public Triangulo(int x, int y, String color,
                      double ladoA, double ladoB, double ladoC, double base, double altura) {
        super(x, y, color);
        this.ladoA = ladoA;
        this.ladoB = ladoB;
        this.ladoC = ladoC;
        this.base = base;
        this.altura = altura;
    }

    @Override
    public double calcularArea() {
        return (base * altura) / 2.0;
    }

    @Override
    public double calcularPerimetro() {
        return ladoA + ladoB + ladoC;
    }
}

// =========================================================
// EL "MOTOR DE RENDERIZADO"
// =========================================================
class Lienzo {
    // Colección dinámica de elementos. Nótese el tipo: ElementoGrafico.
    // Esto es polimorfismo: la lista guarda referencias del tipo base,
    // pero cada objeto real puede ser un Rectangulo, un Circulo, un
    // Triangulo, etc.
    private ArrayList<ElementoGrafico> elementos = new ArrayList<>();

    public void agregarElemento(ElementoGrafico elemento) {
        elementos.add(elemento);
    }

    public ArrayList<ElementoGrafico> getElementos() {
        return elementos;
    }

    // El "motor" recorre todo sin saber (ni necesitar saber) de qué
    // figura concreta se trata.
    // funciona igual sin importar cuántas figuras nuevas se agreguen
    // en el futuro.
    public void renderizar() {
        double areaTotal = 0;
        double perimetroTotal = 0;

        for (ElementoGrafico elemento : elementos) {
            elemento.cambiarColor("#808080");        // filtro escala de grises
            elemento.moverA(0, 0);                     // mover al origen
            areaTotal += elemento.calcularArea();       // compila gracias al contrato
            perimetroTotal += elemento.calcularPerimetro();
        }

        System.out.println("Área total ocupada: " + areaTotal + " píxeles");
        System.out.println("Perímetro total: " + perimetroTotal + " píxeles");
    }
}

// =========================================================
// PROGRAMA PRINCIPAL
// =========================================================
public class Main {
    public static void main(String[] args) {
        Lienzo lienzo = new Lienzo();

        lienzo.agregarElemento(new Rectangulo(10, 20, "#FF0000", 40, 20));
        lienzo.agregarElemento(new Elipse(15, 25, "#00FF00", 30, 15));
        lienzo.agregarElemento(new Cuadrado(5, 5, "#0000FF", 25));
        lienzo.agregarElemento(new Circulo(50, 50, "#FFFF00", 10));
        lienzo.agregarElemento(new Triangulo(0, 0, "#FFA500", 5, 5, 6, 6, 4));

        System.out.println("--- Antes de renderizar ---");
        for (ElementoGrafico e : lienzo.getElementos()) {
            System.out.println(e);
        }

        lienzo.renderizar();

        System.out.println("--- Después de renderizar (todos grises y en 0,0) ---");
        for (ElementoGrafico e : lienzo.getElementos()) {
            System.out.println(e);
        }
    }
}