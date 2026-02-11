package tech.hancharou.currencytracker.core

import android.content.Context
import tech.hancharou.currencytracker.domain.ResourceMapper

class ResourceMapperImpl(private val context: Context) : ResourceMapper {
    override fun getContext(): Context = context
}