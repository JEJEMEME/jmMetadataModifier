package com.jejememe.jmmetadatamodifier

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class Mp4LocationModifierInstrumentedTest {

    private lateinit var context: Context
    private lateinit var mp4LocationModifier: Mp4LocationModifier
    private lateinit var testFile: File
    
    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        mp4LocationModifier = Mp4LocationModifier(context)
        
        // 테스트용 MP4 파일 준비
        setupTestFile()
    }
    
    private fun setupTestFile() {
        // Assets에서 테스트용 MP4 파일을 앱의 파일 디렉토리로 복사
        testFile = File(context.filesDir, "test.mp4")
        context.assets.open("test.mp4").use { input ->
            testFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
    
    @Test
    fun testUpdateLocationWithRealFile() {
        // Given
        val testUri = testFile.toUri()
        val testLatitude = 37.5326f
        val testLongitude = 127.0246f
        
        // When
        val result = mp4LocationModifier.updateLocation(
            inputUri = testUri,
            latitude = testLatitude,
            longitude = testLongitude
        )
        
        // Then
        assertNotNull(result, "위치 정보가 업데이트된 파일이 생성되어야 합니다")
        assertTrue(result.exists(), "생성된 파일이 실제로 존재해야 합니다")
        assertTrue(result.length() > 0, "생성된 파일이 비어있지 않아야 합니다")
    }
    
    @Test
    fun testUpdateLocationWithInvalidUri() {
        // Given
        val invalidUri = Uri.parse("content://invalid/uri")
        
        // When
        val result = mp4LocationModifier.updateLocation(
            inputUri = invalidUri,
            latitude = 37.5326f,
            longitude = 127.0246f
        )
        
        // Then
        kotlin.test.assertNull(result, "잘못된 URI에 대해 null을 반환해야 합니다")
    }
} 