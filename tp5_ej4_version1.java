import java.util.ArrayList;

// =========================================================
// CLASE BASE (ABSTRACTA)
// =========================================================
// Es abstracta porque no tiene sentido crear un "ElementoGrafico"
// genérico: solo existen Rectángulos, Elipses, etc. Pero sí define
// el contrato común que todos los elementos deben cumplir.
abstract class ElementoGrafico {
    protected int x;
    protected int y;
    protected String color;

    public ElementoGrafico(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    // Comportamiento común -> no hace falta que cada subclase lo reimplemente
    public void moverA(int nuevoX, int nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
    }

    public void cambiarColor(String nuevoColor) {
        this.color = nuevoColor;
    }

    // Método abstracto: obliga a cada subclase a implementarlo,
    // pero además queda "registrado" en el contrato de ElementoGrafico.
    // Gracias a esto, el compilador si sabe que cualquier variable
    // de tipo ElementoGrafico tiene un calcularArea() válido.
    public abstract double calcularArea();

    @Override
    public String toString() {
        return String.format("%s [x=%d, y=%d, color=%s, area=%.2f]",
                getClass().getSimpleName(), x, y, color, calcularArea());
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
}

// Un Cuadrado es-un Rectángulo con base == altura (reutilizamos código)
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
}

// Un Círculo es-una Elipse con ambos semiejes iguales (el radio)
class Circulo extends Elipse {
    public Circulo(int x, int y, String color, double radio) {
        super(x, y, color, radio, radio);
    }
}

// =========================================================
// EL "MOTOR DE RENDERIZADO"
// =========================================================
class Lienzo {
    // Colección dinámica de elementos. Nótese el tipo: ElementoGrafico.
    // Esto es POLIMORFISMO: la lista guarda referencias del tipo base,
    // pero cada objeto real puede ser un Rectangulo, un Circulo, etc.
    private ArrayList<ElementoGrafico> elementos = new ArrayList<>();

    public void agregarElemento(ElementoGrafico elemento) {
        elementos.add(elemento);
    }

    public ArrayList<ElementoGrafico> getElementos() {
        return elementos;
    }

    // El "motor" recorre todo sin saber (ni necesitar saber) de qué
    // figura concreta se trata.
    public void renderizar() {
        double areaTotal = 0;

        for (ElementoGrafico elemento : elementos) {
            elemento.cambiarColor("#808080");   // filtro escala de grises
            elemento.moverA(0, 0);              // mover al origen
            areaTotal += elemento.calcularArea();
        }

        System.out.println("Área total ocupada: " + areaTotal + " píxeles");
    }
}

// =========================================================
// PROGRAMA PRINCIPAL
// =========================================================
public class tp5_ej4_version1 {
    public static void main(String[] args) {
        Lienzo lienzo = new Lienzo();

        lienzo.agregarElemento(new Rectangulo(10, 20, "#FF0000", 40, 20));
        lienzo.agregarElemento(new Elipse(15, 25, "#00FF00", 30, 15));
        lienzo.agregarElemento(new Cuadrado(5, 5, "#0000FF", 25));
        lienzo.agregarElemento(new Circulo(50, 50, "#FFFF00", 10));

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