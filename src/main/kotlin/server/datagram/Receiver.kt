package server

import core.data.Request
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketTimeoutException

internal class DatagramReceiver {
    private val receiveArray = ByteArray(256*256)
    private var receiveLength = receiveArray.size
    private var socket = DatagramSocket(5555)
    private var receivePacket = DatagramPacket(receiveArray, receiveLength)

    init {
        socket.soTimeout = 100
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun update() : DataRequest? {
        try {
            socket.receive(receivePacket)
        }
        catch (e : SocketTimeoutException) {
            return null
        }
        val data = ProtoBuf.decodeFromByteArray<Request>(receiveArray)
        val host = receivePacket.address
        val port = receivePacket.port
        return DataRequest(data, host, port)
    }
}
