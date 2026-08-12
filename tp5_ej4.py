class Punto:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __str__(self):
        return f"({self.x:.2f}, {self.y:.2f})"


class ElementoGrafico:
    def __init__(self, color_hex, posicion_centro, nombre_capa):
        self.color_hex = color_hex
        self.posicion_centro = posicion_centro
        self.nombre_capa = nombre_capa

    def mover_a(self, nuevo_destino):
        self.posicion_centro.x = nuevo_destino.x
        self.posicion_centro.y = nuevo_destino.y

    def __str__(self):
        return (f"[{type(self).__name__}] Capa: {self.nombre_capa} | "
                f"Color: {self.color_hex} | Centro: {self.posicion_centro}")


class Rectangulo(ElementoGrafico):
    def __init__(self, color_hex, posicion_centro, nombre_capa, lado_menor, lado_mayor):
        super().__init__(color_hex, posicion_centro, nombre_capa)
        self.lado_menor = lado_menor
        self.lado_mayor = lado_mayor

    def calcular_area(self):
        return self.lado_menor * self.lado_mayor

    def calcular_perimetro(self):
        return 2 * (self.lado_menor + self.lado_mayor)


class Elipse(ElementoGrafico):
    def __init__(self, color_hex, posicion_centro, nombre_capa, radio_mayor, radio_menor):
        super().__init__(color_hex, posicion_centro, nombre_capa)
        self.radio_mayor = radio_mayor
        self.radio_menor = radio_menor

    def calcular_area(self):
        import math
        return math.pi * self.radio_mayor * self.radio_menor


class Cuadrado(Rectangulo):
    def __init__(self, color_hex, posicion_centro, nombre_capa, lado):
        super().__init__(color_hex, posicion_centro, nombre_capa, lado, lado)


class Circulo(Elipse):
    def __init__(self, color_hex, posicion_centro, nombre_capa, radio):
        super().__init__(color_hex, posicion_centro, nombre_capa, radio, radio)


class Lienzo:
    def __init__(self):
        self.elementos = []

    def agregar(self, elemento):
        self.elementos.append(elemento)

    def aplicar_filtro_grises_y_origen(self):
        origen = Punto(0, 0)
        for elemento in self.elementos:
            elemento.color_hex = "#808080"
            elemento.mover_a(origen)

    def area_total(self):
        total = 0
        for elemento in self.elementos:
            total += elemento.calcular_area()  # <-- el intento "problemático" del TP
        return total


lienzo = Lienzo()
lienzo.agregar(Rectangulo("#FF0000", Punto(10, 10), "Capa1", 5, 10))
lienzo.agregar(Elipse("#00FF00", Punto(20, 20), "Capa1", 8, 4))
lienzo.agregar(Cuadrado("#0000FF", Punto(5, 5), "Capa2", 6))
lienzo.agregar(Circulo("#FFFF00", Punto(0, 0), "Capa2", 3))

lienzo.aplicar_filtro_grises_y_origen()
print(f"Área total: {lienzo.area_total():.2f}")