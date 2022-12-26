package app.wedeliveradd

class Package(
    packageType: PackageTypesEnum,
    isFragile: Boolean,
    weight: Double,
    coor: Coordinate,
    name: String,
    address: String,
    statusEnum: PackageStatusEnum
) {
    var id : String = ""
        get() = field

    var status: PackageStatusEnum = statusEnum

    var deliveryGuy: String = ""

    var packageType: PackageTypesEnum = packageType
        get() = field
        private set(value) {
            field = value
        }
    var isFragile: Boolean = isFragile
        get() = field
        private set(value) {
            field = value
        }
    var weight: Double = weight
        get() = field
        private set(value) {
            field = value
        }
    var coordinate: Coordinate = coor
        get() = field
        private set(value) {
            field = value
        }
    var name: String = name
        get() = field
        private set(value) {
            field = value
        }
    var address: String = address
        get() = field
        private set(value) {
            field = value
        }
}