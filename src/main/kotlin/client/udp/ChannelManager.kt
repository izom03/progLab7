package client.udp

import core.data.Request
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.ByteArrayInputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.DatagramChannel
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

class StreamManager {
    private val serverAddress = "localhost" // Адрес сервера
    private val serverPort = 5555 // Порт сервера

    private val receiveBuffer: ByteBuffer = ByteBuffer.allocate(256*256)
    private var sendBuffer: ByteBuffer = ByteBuffer.allocate(0)
    private val inStream = ByteArrayInputStream(receiveBuffer.array())
    private val charBuffer = CharBuffer.allocate(256*256)
    private val address = InetSocketAddress(serverAddress, serverPort)
    private val channel = DatagramChannel.open()

    init {
        channel.configureBlocking(false)
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun send(request : Request) {
        val byteArray = ProtoBuf.encodeToByteArray(request)
        sendBuffer = ByteBuffer.wrap(byteArray)
        channel.send(sendBuffer, address)
        sendBuffer.clear()
    }
}