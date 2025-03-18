package com.jejememe.jmmetadatamodifier

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class Mp4LocationModifierTest {
    
    private lateinit var context: Context
    private lateinit var mp4LocationModifier: Mp4LocationModifier
    private lateinit var testFile: File
    
    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        mp4LocationModifier = Mp4LocationModifier(context)
        
        // 테스트 파일 준비
        testFile = File(context.filesDir, "test.mp4")
        testFile.createNewFile()
        testFile.writeBytes(ByteArray(1024))
    }
    
    @Test
    fun `test updateLocation with valid uri`() {
        // Given
        val testLatitude = 37.5326f
        val testLongitude = 127.0246f
        val testUri = Uri.fromFile(testFile)
        
        // When
        val result = mp4LocationModifier.updateLocation(
            inputUri = testUri,
            latitude = testLatitude,
            longitude = testLongitude
        )
        
        // Then
        assertNotNull(result, "위치 정보 업데이트 결과가 null이 아니어야 합니다")
    }
    
    @Test
    fun `test updateLocation with invalid uri`() {
        // Given
        val testLatitude = 37.5326f
        val testLongitude = 127.0246f
        val invalidUri = Uri.parse("file:///invalid/path.mp4")
        
        // When
        val result = mp4LocationModifier.updateLocation(
            inputUri = invalidUri,
            latitude = testLatitude,
            longitude = testLongitude
        )
        
        // Then
        assertNull(result, "잘못된 URI에 대해 null을 반환해야 합니다")
    }
} 