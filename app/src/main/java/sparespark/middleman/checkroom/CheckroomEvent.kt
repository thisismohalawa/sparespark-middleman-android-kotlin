package sparespark.middleman.checkroom

sealed class CheckroomEvent {
    data object GetProducts : CheckroomEvent()
    data class OnImgClearItemClick(val position: Int) : CheckroomEvent()
    data class OnImgProductItemClick(val position: Int) : CheckroomEvent()
    data object OnWhatsAppBuyBtnClick : CheckroomEvent()
}
