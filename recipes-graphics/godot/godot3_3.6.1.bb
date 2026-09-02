# Godot 3 is kept alongside Godot 4 for legacy GLES2-class targets.
# On Linux, upstream Godot 3 uses X11/GLX; run it under XWayland on a
# Wayland-only compositor.

SUMMARY = "Godot 3 game engine runtime with the GLES2 renderer"
DESCRIPTION = "Godot 3.6 LTS runtime packaged separately for legacy OpenGL/GLES projects"
HOMEPAGE = "https://github.com/godotengine/godot"
BUGTRACKER = "https://github.com/godotengine/godot/issues"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=429628d6598a258acb0c2524e08a3036"

DEPENDS = " \
    python3-scons-native \
    libx11 \
    libxcursor \
    libxinerama \
    libxext \
    libxrandr \
    libxrender \
    libxi \
    virtual/libgl \
"

SRCREV = "b1ba98fced19ac05b7a39b64a97dd7b1005cb7bb"
SRC_URI = "git://github.com/godotengine/godot.git;protocol=https;lfs=0;branch=3.6"

S = "${WORKDIR}/git"

inherit pkgconfig features_check

REQUIRED_DISTRO_FEATURES = "x11 opengl"

COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
COMPATIBLE_MACHINE:armv7 = "(.*)"
COMPATIBLE_MACHINE:armv7a = "(.*)"
COMPATIBLE_MACHINE:armv7ve = "(.*)"
COMPATIBLE_MACHINE:x86 = "(.*)"
COMPATIBLE_MACHINE:x86-64 = "(.*)"

GODOT3_ARCH:aarch64 = "arm64"
GODOT3_ARCH:armv7 = "arm32"
GODOT3_ARCH:armv7a = "arm32"
GODOT3_ARCH:armv7ve = "arm32"
GODOT3_ARCH:x86 = "x86"
GODOT3_ARCH:x86-64 = "x86_64"

GODOT3_BITS = "32"
GODOT3_BITS:aarch64 = "64"
GODOT3_BITS:x86-64 = "64"

do_compile() {
    cd ${S}
    scons platform=x11 target=release_debug tools=no \
        arch=${GODOT3_ARCH} bits=${GODOT3_BITS} \
        use_static_cpp=yes optimize=speed progress=yes \
        pulseaudio=no speechd=no alsa=no udev=yes touch=yes \
        builtin_freetype=yes builtin_libpng=yes builtin_zlib=yes \
        num_jobs=${BB_NUMBER_THREADS} \
        CC="${CC}" CFLAGS="${CFLAGS}" \
        CXX="${CXX}" CXXFLAGS="${CXXFLAGS}" \
        AR="${AR}" RANLIB="${RANLIB}" \
        PKG_CONFIG="${PKG_CONFIG}"
}

do_install() {
    runtime="$(find ${S}/bin -maxdepth 1 -type f -name 'godot.x11*' | head -n 1)"
    if [ -z "$runtime" ]; then
        bbfatal "Godot 3 runtime was not produced"
    fi

    install -d ${D}${bindir}
    install -m 0755 "$runtime" ${D}${bindir}/godot3
}

RDEPENDS:${PN} = " \
    libx11 \
    libxcursor \
    libxinerama \
    libxext \
    libxrandr \
    libxrender \
    libxi \
"

INSANE_SKIP:${PN} = "already-stripped"
