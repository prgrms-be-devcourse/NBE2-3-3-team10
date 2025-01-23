package org.washcode.washpang.domain.review.entity

import jakarta.persistence.*
import org.washcode.washpang.domain.pickup.entity.Pickup
import java.sql.Timestamp

@Entity
class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Int =0

    @ManyToOne
    @JoinColumn(name="pickupId")
    lateinit var pickup: Pickup //(**확인필요**)

    var content:String = ""
    var createAt: Timestamp= Timestamp(System.currentTimeMillis())
}