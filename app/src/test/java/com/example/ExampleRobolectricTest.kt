package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.TemplatesRepository
import com.example.data.local.DesignConverters
import com.example.model.TemplateCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CoverCraft", appName)
    }

    @Test
    fun `verify default templates available across categories`() {
        val templates = TemplatesRepository.templates
        assertTrue(templates.isNotEmpty())

        val bookCovers = templates.filter { it.category == TemplateCategory.BOOK_COVER }
        val socialPosts = templates.filter { it.category == TemplateCategory.SOCIAL_MEDIA }
        val businessCards = templates.filter { it.category == TemplateCategory.BUSINESS_CARD }

        assertTrue("Book covers must be available", bookCovers.isNotEmpty())
        assertTrue("Social media posts must be available", socialPosts.isNotEmpty())
        assertTrue("Business cards must be available", businessCards.isNotEmpty())
    }

    @Test
    fun `verify design converters serialize and deserialize properly`() {
        val converters = DesignConverters()
        val original = TemplatesRepository.templates.first().designData
        val json = converters.fromDesignData(original)
        assertNotNull(json)
        assertTrue(json.contains(original.title))

        val restored = converters.toDesignData(json)
        assertNotNull(restored)
        assertEquals(original.title, restored?.title)
        assertEquals(original.badgeText, restored?.badgeText)
    }
}
