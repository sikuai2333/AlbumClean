package com.sikuai.album.domain.usecase

import com.sikuai.album.data.local.PhotoEntity
import javax.inject.Inject

class GroupPhotosUseCase @Inject constructor() {

    /**
     * Groups a list of photos into sublists of a specified size.
     *
     * @param photos The flat list of photos to group.
     * @param groupSize The desired size of each group.
     * @return A list of lists, where each inner list is a group of photos.
     */
    operator fun invoke(photos: List<PhotoEntity>, groupSize: Int): List<List<PhotoEntity>> {
        if (groupSize <= 0) {
            return listOf(photos) // Return the whole list as a single group if group size is invalid
        }
        return photos.chunked(groupSize)
    }
}
