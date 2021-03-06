/*
 * Copyright 2017 Masteroreo77
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.neomccreations.micmap.voxel

import com.neomccreations.micmap.coordutil.xyzToIndex
import com.neomccreations.micmap.schematic.Schematic
import com.neomccreations.micmap.voxel.optimize.Optimization

/**
 * @author Falcinspire
 * @version Apr/29/2017 (8:32 PM)
 */

class VoxelSpace(val width : Short, val length : Short, val height : Short, val space : Array<Voxel>) {

    fun optimize(optimization : Optimization) {
        optimization.optimize(this)
    }

    fun forAll(operation : (x: Int, y: Int, z: Int, index: Int, voxel : Voxel) -> Unit) {
        for (y in 0 until height) {
            for (z in 0 until length) {
                for (x in 0 until width) {
                    val rawIndex = xyzToIndex(x, y, z, width, length)
                    operation(x, y, z, rawIndex, space[rawIndex])
                }
            }
        }
    }
}

//TODO maybe move this elsewhere
fun voxelSpaceFromSchematic(schem : Schematic) : VoxelSpace {

    //init the array with air voxels
    val array = Array<Voxel>(
            schem.width * schem.length * schem.height,
            {index -> Voxel(schem.blocks[index])})

    return VoxelSpace(schem.width, schem.length, schem.height, array)
}

    