package app.wedeliveradd

class Coordinate(Longitude: Double, Latitude: Double) {
    var longitude: Double = 0.0
        get() = field
        set(value) {
            field = value
        }
    var latitude: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    init {
        latitude = Latitude
        longitude = Longitude
    }
}