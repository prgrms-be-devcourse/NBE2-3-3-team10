package org.washcode.washpang.domain.laundryshop.entity

import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.washcode.washpang.domain.user.entity.User
import java.sql.Timestamp

@Data
@Entity
class LaundryShop{

    //세탁소
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id") // 외래 키 설정
    var user: User? = null

    var shopName: String? = null
    var businessNumber: String? = null
    var userName: String? = null
    var phone: String? = null
    var address: String? = null
    var nonOperatingDays: String? = null

    var latitude: Double? = null
    var longitude: Double? = null

    @CreationTimestamp
    var createdAt: Timestamp? = null
}
