package com.ayushsabharwal.navigationdrawer.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ayushsabharwal.navigationdrawer.R
import com.ayushsabharwal.navigationdrawer.databinding.ItemAppBinding
import com.ayushsabharwal.navigationdrawer.databinding.ItemHeaderBinding
import com.ayushsabharwal.navigationdrawer.databinding.ItemProfileHeaderBinding
import com.ayushsabharwal.navigationdrawer.databinding.ItemSeeMoreBinding
import com.ayushsabharwal.navigationdrawer.model.MenuItemModel
import com.bumptech.glide.Glide

sealed class DrawerItem {
    data class Profile(val title: String, val userPhoto: String) : DrawerItem()
    data class Header(val title: String) : DrawerItem()
    data class App(val item: MenuItemModel) : DrawerItem()
    object SeeMore : DrawerItem()
}

class DrawerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userName = ""
    private var userPhoto = ""

    fun setProfile(name: String, photo: String) {
        userName = name
        userPhoto = photo
    }

    companion object {
        private const val TYPE_PROFILE = 0
        private const val TYPE_HEADER = 1
        private const val TYPE_APP = 2
        private const val TYPE_SEE_MORE = 3
    }

    private val items = mutableListOf<DrawerItem>()
    private var expanded = false
    private var appsList = listOf<MenuItemModel>()

    fun setMenus(menus: List<MenuItemModel>) {
        items.clear()
        expanded = false

        items.add(
            DrawerItem.Profile(
                title = userName,
                userPhoto = userPhoto
            )
        )

        val topMenus = mutableListOf<MenuItemModel>()
        val appsMenus = mutableListOf<MenuItemModel>()
        val helpMenus = mutableListOf<MenuItemModel>()

        var currentSection = ""

        menus.forEach { menu ->
            if (menu.type == 0) {
                currentSection = menu.label
            } else {
                when (currentSection) {
                    "" -> topMenus.add(menu)
                    "APPS" -> appsMenus.add(menu)
                    "HELP & MORE" -> helpMenus.add(menu)
                }
            }
        }

        topMenus.forEach {
            items.add(DrawerItem.App(it))
        }

        items.add(DrawerItem.Header("APPS"))

        appsList = appsMenus
        items.addAll(appsMenus.take(4).map { DrawerItem.App(it) })

        if (appsMenus.size > 4) {
            items.add(DrawerItem.SeeMore)
        }

        items.add(DrawerItem.Header("HELP & MORE"))

        helpMenus.forEach {
            items.add(DrawerItem.App(it))
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = when (items[position]) {
        is DrawerItem.Profile -> TYPE_PROFILE
        is DrawerItem.Header -> TYPE_HEADER
        is DrawerItem.App -> TYPE_APP
        is DrawerItem.SeeMore -> TYPE_SEE_MORE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_PROFILE -> ProfileVH(
            ItemProfileHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        TYPE_HEADER -> HeaderVH(
            ItemHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        TYPE_APP -> AppVH(
            ItemAppBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        else -> SeeMoreVH(
            ItemSeeMoreBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = items.size

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {

            is DrawerItem.Profile -> {
                val binding = (holder as ProfileVH).binding
                Glide.with(binding.root).load(item.userPhoto).circleCrop().into(binding.ivProfile)
                binding.tvName.text = item.title
            }

            is DrawerItem.Header -> {
                (holder as HeaderVH).binding.tvHeader.text = item.title
            }

            is DrawerItem.App -> {
                val binding = (holder as AppVH).binding
                binding.tvTitle.text = item.item.label
                Glide.with(binding.root).load(item.item.icon).into(binding.ivIcon)
            }

            is DrawerItem.SeeMore -> {
                (holder as SeeMoreVH).binding.root.setOnClickListener {
                    if (expanded) return@setOnClickListener
                    val seeMoreIndex = items.indexOfFirst { it is DrawerItem.SeeMore }
                    if (seeMoreIndex == -1) return@setOnClickListener
                    items.removeAt(seeMoreIndex)
                    items.addAll(
                        seeMoreIndex,
                        appsList.drop(4).map { DrawerItem.App(it) }
                    )
                    expanded = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    class ProfileVH(val binding: ItemProfileHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    class HeaderVH(val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    class AppVH(val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root)

    class SeeMoreVH(val binding: ItemSeeMoreBinding) : RecyclerView.ViewHolder(binding.root)
}