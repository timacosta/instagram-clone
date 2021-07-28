package io.keepcoding.instagram_clone

data class Gallery(
   val data: List<Image>,
   val success: Boolean,
   val status: Int,
) {
    data class Image(
        val id: String,
        val title: String?,
        val description: String?,
        val datetime: Int,
        val cover: String?,
        val cover_width: Int?,
        val cover_height: Int?,
        val account_url: String?,
        val account_id: Int?,
        val link: String,
        val ups: Int?,
        val downs: Int?,
        val points: Int?,
        val score: Int?,
        val is_album: Boolean?,
        val favorite: Boolean?,
        val nsfw: Boolean?,
        val section: String?,
        val comment_count: Int?,
        val favorite_count: Int?,
        val in_gallery: Boolean,
        val tags: List<Tag>,
        val in_most_viral: Boolean,
        val images: List<Image>?,
        val type: String?,
        val animated: Boolean?,
        val width: Int?,
        val height: Int?,
        val size: Int?,
        val bandwidth: Long?,
        val has_sound: Boolean?,
        val edited: Int?,
        val mp4: String?,
        val gify: String?,
        val hls: String?,
        val mp4_size: Int?,
        val looping: Boolean?,
        val processing: Processing?,
    ) {
        data class Tag(
            val name: String,
            val display_name: String,
            val followers: Int,
            val total_items: Int,
            val following: Boolean,
            val is_whitelisted: Boolean,
            val background_hash: String,
            val thumbnail_hash: String?,
            val accent: String?,
            val background_is_animated: Boolean,
            val thumbnail_is_animated: Boolean,
            val is_promoted: Boolean,
            val description: String,
            val logo_hash: String?,
            val logo_destination_url: String?
        )

        data class Processing(
            val status: String,
        )
    }
}