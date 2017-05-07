package com.neomccreations.micmap.voxel

import com.neomccreations.micmap.schematic.Schematic

/**
 * @author Falcinspire
 * @version Apr/29/2017 (8:32 PM)
 */

class VoxelSpace(val width : Short, val length : Short, val height : Short, val space : Array<Voxel>) {

    //TODO optimize this brute-force method
    fun cullOptimize() {

        for (y in 0 until height) {
            for (z in 0 until length) {
                for (x in 0 until width) {
                    val rawIndex = xyzToIndex(x, y, z)

                    val voxel = space[rawIndex]
                    if (voxel.opaque) {

                        voxel.south = !((z + 1 < length) && space[xyzToIndex(x, y, z + 1)].opaque)
                        voxel.north = !((z - 1 >= 0) && space[xyzToIndex(x, y, z - 1)].opaque)
                        voxel.east = !((x + 1 < width) && space[xyzToIndex(x + 1, y, z)].opaque)
                        voxel.west = !((x - 1 >= 0) && space[xyzToIndex(x - 1, y, z)].opaque)
                        voxel.up = !((y + 1 < height) && space[xyzToIndex(x, y + 1, z)].opaque)
                        voxel.down = !((y - 1 >= 0) && space[xyzToIndex(x, y - 1, z)].opaque)

                    }

                }
            }
        }

        for (i in 0 until space.size) {
            space[i].setNotOpaqueIfEmpty()
        }

    }

    inline fun xyzToIndex(x : Int, y : Int, z : Int) : Int {
        return y * width * length + z * width + x
    }

    fun forAll(operation : (x: Int, y: Int, z: Int, index: Int, voxel : Voxel) -> Unit) {
        for (y in 0 until height) {
            for (z in 0 until length) {
                for (x in 0 until width) {
                    val rawIndex = xyzToIndex(x, y, z)
                    operation(x, y, z, rawIndex, space[rawIndex])
                }
            }
        }
    }
}

//TODO maybe move this elsewhere
fun voxelSpaceFromSchematic(schem : Schematic) : VoxelSpace {

    //init the array with opaque voxels
    val array = Array<Voxel>(
            schem.width * schem.length * schem.height,
            {i -> Voxel(opaque = true)})

    //make all of the air blocks non-opaque
    schem.forAllBlocks(
            {x, y, z, index, id, data ->
                if (id == 0.toShort()) array[index].opaque = false })

    return VoxelSpace(schem.width, schem.length, schem.height, array)
}

    