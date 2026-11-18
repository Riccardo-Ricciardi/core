package org.simpmusic.lyrics.parser

import kotlin.test.Test
import kotlin.test.assertEquals

class SyncedLyricsParsingTest {
    private fun starts(data: String): List<Long> =
        parseSyncedLyrics(data).lyrics?.lines?.map { it.startTimeMs.toLong() }.orEmpty()

    private fun texts(data: String): List<String> =
        parseSyncedLyrics(data).lyrics?.lines?.map { it.words }.orEmpty()

    @Test
    fun plainTimestampsAreUnchanged() {
        assertEquals(listOf(12_340L, 15_000L), starts("[00:12.34]one\n[00:15.00]two"))
    }

    @Test
    fun offsetTagShiftsEveryLine() {
        assertEquals(listOf(2_500L), starts("[offset:500]\n[00:02.00]late"))
        assertEquals(listOf(1_500L), starts("[offset:-500]\n[00:02.00]early"))
    }

    @Test
    fun millisecondPrecisionIsAccepted() {
        assertEquals(listOf(12_345L), starts("[00:12.345]three digits"))
    }

    @Test
    fun repeatedTimestampsOnOneLineAreAllKept() {
        assertEquals(listOf(10_000L, 20_000L), starts("[00:10.00][00:20.00]chorus"))
        assertEquals(listOf("chorus", "chorus"), texts("[00:10.00][00:20.00]chorus"))
    }

    @Test
    fun linesAreSortedByStart() {
        assertEquals(listOf(1_000L, 2_000L), starts("[00:02.00]b\n[00:01.00]a"))
    }

    @Test
    fun offsetLineIsNotEmittedAsLyrics() {
        assertEquals(listOf("only"), texts("[offset:250]\n[00:01.00]only"))
    }
}
