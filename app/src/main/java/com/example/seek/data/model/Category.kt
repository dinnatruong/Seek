package com.example.seek.data.model

import com.example.seek.R

data class CategoryItem (
    val titleId: Int,
    val iconId: Int? = null,
    val backgroundId: Int? = null
)

enum class Category {
    RANDOM,
    EDUCATIONAL,
    SOCIAL,
    DIY,
    MUSIC,
    RELAXATION,
    CHARITY,
    COOKING,
    BUSYWORK,
    RECREATIONAL;

    companion object {
        val itemMap = linkedMapOf(
            RANDOM to CategoryItem(
                R.string.random,
                R.drawable.ic_favorite_24dp,
                R.color.slate
            ),
            EDUCATIONAL to CategoryItem(
                R.string.educational,
                R.drawable.ic_favorite_24dp,
                R.color.pink
            ),
            SOCIAL to CategoryItem(
                R.string.social,
                R.drawable.ic_favorite_24dp,
                R.color.blue
            ),
            DIY to CategoryItem(
                R.string.diy,
                R.drawable.ic_favorite_24dp,
                R.color.green
            ),
            MUSIC to CategoryItem(
                R.string.music,
                R.drawable.ic_favorite_24dp,
                R.color.purple_200
            ),
            RELAXATION to CategoryItem(
                R.string.relaxation,
                R.drawable.ic_favorite_24dp,
                R.color.concrete
            ),
            CHARITY to CategoryItem(
                R.string.charity,
                R.drawable.ic_favorite_24dp,
                R.color.lime
            ),
            COOKING to CategoryItem(
                R.string.cooking,
                R.drawable.ic_favorite_24dp,
                R.color.salmon
            ),
            BUSYWORK to CategoryItem(
                R.string.busywork,
                R.drawable.ic_favorite_24dp,
                R.color.gray
            ),
            RECREATIONAL to CategoryItem(
                R.string.recreational,
                R.drawable.ic_favorite_24dp,
                R.color.sky
            )
        )
    }
}