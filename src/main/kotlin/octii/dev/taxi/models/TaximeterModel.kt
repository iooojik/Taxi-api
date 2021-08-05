package octii.dev.taxi.models

import javax.persistence.*


data class TaximeterModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    var id: Long = -1,

)