package sparespark.middleman.core.mapping

import com.google.firebase.auth.FirebaseUser
import sparespark.middleman.core.DEACTIVATED
import sparespark.middleman.core.DEF_EMAIL
import sparespark.middleman.core.DEF_NAME
import sparespark.middleman.data.room.user.RoomUser
import sparespark.middleman.data.model.user.RemoteUser
import sparespark.middleman.data.model.user.User

internal val FirebaseUser.toUser: User
    get() = User(
        uid = this.uid,
        name = this.displayName ?: "user",
        email = this.email ?: "",
        activated = true
    )
internal val RemoteUser.toUser: User
    get() = User(
        uid = this.uid ?: "",
        name = this.name ?: DEF_NAME,
        email = this.email ?: DEF_EMAIL,
        activated = this.activated ?: DEACTIVATED
    )
internal val RoomUser.toUser: User
    get() = User(
        uid = this.uid,
        name = this.name,
        email = this.email,
        activated = this.activated
    )
internal val User.toRoomUser: RoomUser
    get() = RoomUser(
        this.uid,
        this.name,
        this.email,
        this.activated
    )
