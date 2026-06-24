"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { BarChart2, BookOpen, FlaskConical } from "lucide-react"
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
  SidebarSeparator,
} from "@/components/ui/sidebar"

const navItems = [
  {
    label: "BenchMark",
    href: "/benchmark",
    icon: BarChart2,
    children: [
      { label: "Análisis", href: "/benchmark/analisis" },
      { label: "Mapa", href: "/benchmark/map" },
    ],
  },
  {
    label: "Caso de Estudio",
    href: "/casodeestudio",
    icon: BookOpen,
  },
]

export function AppSidebar() {
  const pathname = usePathname()

  return (
    <Sidebar collapsible="icon">
      <SidebarHeader className="px-3 py-4">
        <div className="flex items-center gap-3 px-1">
          <div className="flex size-7 shrink-0 items-center justify-center rounded-md bg-sidebar-accent">
            <FlaskConical className="size-[15px] text-sidebar-ring" />
          </div>
          <span
            className="truncate text-[13px] font-bold tracking-tight text-sidebar-accent-foreground"
            style={{ fontFamily: "var(--font-syne)" }}
          >
            Laboratorio 2026
          </span>
        </div>
      </SidebarHeader>

      <SidebarSeparator />

      <SidebarContent className="pt-1">
        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {navItems.map(({ label, href, icon: Icon, children }) => {
                const isActive =
                  pathname === href || pathname.startsWith(href + "/")
                return (
                  <SidebarMenuItem key={href}>
                    <SidebarMenuButton
                      render={<Link href={href} />}
                      isActive={isActive}
                      tooltip={label}
                      size="default"
                    >
                      <Icon />
                      <span>{label}</span>
                    </SidebarMenuButton>

                    {children?.length ? (
                      <SidebarMenuSub>
                        {children.map(({ label: childLabel, href: childHref }) => (
                          <SidebarMenuSubItem key={childHref}>
                            <SidebarMenuSubButton
                              render={<Link href={childHref} />}
                              isActive={pathname === childHref}
                            >
                              <span>{childLabel}</span>
                            </SidebarMenuSubButton>
                          </SidebarMenuSubItem>
                        ))}
                      </SidebarMenuSub>
                    ) : null}
                  </SidebarMenuItem>
                )
              })}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  )
}
