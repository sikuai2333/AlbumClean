package com.sikuai.album.util

import com.sikuai.album.data.local.PhotoEntity

// 注意: 这是一个在ViewModel之间传递数据的临时解决方案。
// 在生产级应用中，应使用更健壮的方案，如共享的Repository或通过导航参数传递ID。
object TmpDataHolder {
    var keptPhotos: List<PhotoEntity> = emptyList()
    var deletedPhotos: List<PhotoEntity> = emptyList()
}
