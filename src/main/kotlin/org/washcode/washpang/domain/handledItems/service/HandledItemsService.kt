package org.washcode.washpang.domain.handledItems.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.washcode.washpang.domain.handledItems.entity.HandledItems
import org.washcode.washpang.domain.handledItems.repository.HandledItemsRepository

@Service
class HandledItemsService {
    @Autowired
    private val handledItemsRepository: HandledItemsRepository? = null

    fun getHandledItemById(id: Long): HandledItems {
        // itemId로 데이터베이스에서 아이템을 조회
        return handledItemsRepository!!.findById(id)
            .orElseThrow {
                IllegalArgumentException(
                    "아이템을 찾을 수 없습니다. ID: $id"
                )
            }!!
    }

    fun getAllHandledItems(laundryShopId: Long?): List<HandledItems?>? {
        try {
            return handledItemsRepository!!.findByLaundryshopId(laundryShopId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("HandledItems 조회 중 오류 발생", e)
        }
    }
}
