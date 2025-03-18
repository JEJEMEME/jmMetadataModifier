package com.jejememe.jmmetadatamodifier

import android.content.Context
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.net.Uri
import java.io.File
import java.nio.ByteBuffer

class Mp4LocationModifier(private val context: Context) {

    /**
     * 주어진 입력 mp4 파일(URI)의 위치 메타데이터를 변경하여 새 파일을 반환합니다.
     *
     * @param inputUri    원본 mp4 파일의 URI
     * @param latitude    추가할 위도 (예: 37.5326f)
     * @param longitude   추가할 경도 (예: 127.0246f)
     * @return            변경된 파일을 가리키는 File 객체 (실패 시 null)
     */
    fun updateLocation(inputUri: Uri, latitude: Float, longitude: Float): File? {
        // 작업할 디렉터리 및 파일 이름 설정
        val outputDir = context.getExternalFilesDir(null)
        val tempInputFile = File(outputDir, "temp_input_${System.currentTimeMillis()}.mp4")
        val outputFile = File(outputDir, "location_updated_${System.currentTimeMillis()}.mp4")

        try {
            context.contentResolver.openInputStream(inputUri)?.use { input ->
                tempInputFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        if (!tempInputFile.exists() || tempInputFile.length() == 0L) {
            return null
        }

        try {
            val extractor = MediaExtractor()
            extractor.setDataSource(tempInputFile.absolutePath)

            val muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            muxer.setLocation(latitude, longitude)

            val trackIndexMap = HashMap<Int, Int>()
            for (i in 0 until extractor.trackCount) {
                extractor.selectTrack(i)
                val format = extractor.getTrackFormat(i)
                val trackIndex = muxer.addTrack(format)
                trackIndexMap[i] = trackIndex
                extractor.unselectTrack(i)
            }

            muxer.start()

            val bufferSize = 1024 * 1024
            val buffer = ByteBuffer.allocate(bufferSize)
            val bufferInfo = MediaCodec.BufferInfo()

            // 각 트랙의 샘플 데이터를 새 파일로 복사
            for (i in 0 until extractor.trackCount) {
                extractor.selectTrack(i)
                extractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                val muxerTrackIndex = trackIndexMap[i] ?: continue

                while (true) {
                    val sampleSize = extractor.readSampleData(buffer, 0)
                    if (sampleSize < 0) break

                    bufferInfo.apply {
                        size = sampleSize
                        presentationTimeUs = extractor.sampleTime
                        offset = 0
                        flags = extractor.sampleFlags
                    }
                    muxer.writeSampleData(muxerTrackIndex, buffer, bufferInfo)
                    extractor.advance()
                }
                extractor.unselectTrack(i)
            }

            // 마무리 작업
            muxer.stop()
            muxer.release()
            extractor.release()

            // 작업 완료 후 임시 파일 삭제
            tempInputFile.delete()

            return outputFile
        } catch (e: Exception) {
            e.printStackTrace()
            tempInputFile.delete()
            return null
        }
    }
}