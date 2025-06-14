package rooms

import com.google.gson.Gson
import java.io.InputStreamReader

object RoomLoader {

    private val gson = Gson()

    /**
     * Loads a room from the resources folder.
     */
    fun loadRoom(roomName: String): Pair<RoomMeta, BlockStructure> {
        val loader = javaClass.classLoader
        val stream = loader.getResourceAsStream("rooms/${roomName}/meta.json")

        val metaReader = InputStreamReader(stream!!, Charsets.UTF_8)
        val meta = gson.fromJson(metaReader, RoomMeta::class.java)
        metaReader.close()

        val blocksReader =
            InputStreamReader(loader.getResourceAsStream("rooms/${roomName}/blocks.json")!!, Charsets.UTF_8)
        val blocks = gson.fromJson(blocksReader, BlockStructure::class.java)
        blocksReader.close()

        return Pair(meta, blocks)
    }

    /**
     * Loads the blocks of the lobby.
     */
    fun loadLobby(): Map<String, Map<String, String>> {
        val loader = javaClass.classLoader
        val stream = loader.getResourceAsStream("lobby/lobby_data.json")

        val blocksReader =
            InputStreamReader(stream!!, Charsets.UTF_8)
        val blocks = gson.fromJson(blocksReader, Map::class.java) as Map<String, Map<String, String>>
        blocksReader.close()

        return blocks
    }
}