package com.ikbo0621.anitree.data

import com.ikbo0621.anitree.tree.positioning.RValue

class Layout : RelativeValuesLayout() {
    // Default size: 1080 x 2560
    override val dimens = hashMapOf(
        "heading_text_size" to RValue(0.022f, RValue.Type.SmallSide), // 1080
        "extra_large_text_size" to RValue(0.037f, RValue.Type.SmallSide), // 1080
        "large_text_size" to RValue(0.037f, RValue.Type.SmallSide), // 1080
        "background_text_size" to RValue(0.185f, RValue.Type.SmallSide), // 1080
        "background_name_size" to RValue(0.148f, RValue.Type.SmallSide), // 1080
        "first_anime_word_size" to RValue(0.212f, RValue.Type.SmallSide), // 1080
        "small_text_size" to RValue(0.016f, RValue.Type.SmallSide), // 1080

        "account_fragment_second_name_width" to RValue(0.277f, RValue.Type.X), // 1080
        "account_fragment_first_name_width" to RValue(0.342f, RValue.Type.X), // 1080
        "account_fragment_first_name_height" to RValue(0.259f, RValue.Type.X), // 1080

        "btn_height" to RValue(0.0156f, RValue.Type.Y), // 2560

        "tree_item_size" to RValue(0.1f, RValue.Type.SmallSide), // 1080

        "avatar_item_width" to RValue(0.241f, RValue.Type.X), // 1080
        "avatar_item_height" to RValue(0.1172f, RValue.Type.Y), // 2560


        "icon_size" to RValue(0.037f, RValue.Type.SmallSide), // 1080
        "avatar_account_image_size" to RValue(0.166f, RValue.Type.SmallSide), // 1080

        "search_bar_width" to RValue(0.333f, RValue.Type.X), // 1080
        "search_bar_height" to RValue(0.1016f, RValue.Type.Y), // 2560
        "avatar_icon_size" to RValue(0.055f, RValue.Type.SmallSide), // 1080
        "search_bar_left_line_height" to RValue(0.1289f, RValue.Type.Y), // 2560

        "anime_image_width" to RValue(0.01f, RValue.Type.X), // 1080
        "anime_image_height" to RValue(0.0938f, RValue.Type.Y), // 2560

        "account_upper_rect_height" to RValue(0.1094f, RValue.Type.Y), // 2560

        "anime_upper_rect_height" to RValue(0.1758f, RValue.Type.Y), // 2560
        "anime_lower_rect_height" to RValue(0.1367f, RValue.Type.Y), // 2560

        "search_upper_rect_height" to RValue(0.1719f, RValue.Type.Y), // 2560
        "search_lower_rect_height" to RValue(0.1406f, RValue.Type.Y), // 2560

        "item_tree_width" to RValue(0.167f, RValue.Type.X), // 1080
        "item_tree_img_size" to RValue(0.138f, RValue.Type.SmallSide), // 1080

        "anime_first_word_height" to RValue(0.105f, RValue.Type.Y), // 2560


        "text_size" to RValue(0.3322f, RValue.Type.SmallSide), // 1080

        "line_width" to RValue(0.0166f, RValue.Type.X), // 1080
        "width_of_line" to RValue(0.001f, RValue.Type.X) // 1080
    )
}