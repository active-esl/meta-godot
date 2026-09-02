# Godot 3 is kept alongside Godot 4 for GLES2-class embedded targets.
# FRT supplies the SDL2 platform port, avoiding the desktop GL/GLX dependency
# of upstream Godot 3's Linux/X11 platform.

SUMMARY = "Godot 3 runtime for GLES2 embedded Linux targets"
DESCRIPTION = "Godot 3.6 runtime using the FRT SDL2 platform with native GLES2 contexts"
HOMEPAGE = "https://github.com/efornara/frt"
BUGTRACKER = "https://github.com/efornara/frt/issues"
SECTION = "graphics"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=429628d6598a258acb0c2524e08a3036"

DEPENDS = " \
    python3-scons-native \
    libsdl2 \
    zlib \
"

SRC_URI = " \
    git://github.com/efornara/godot3.git;protocol=https;lfs=0;branch=frt;name=godot \
    git://github.com/efornara/frt.git;protocol=https;lfs=0;branch=master;name=frt;destsuffix=git/platform/frt \
    file://0001-frt-use-pkg-config-for-sdl2.patch \
"

# FRT 3.6.3-1 and its matching Godot 3 fork, pinned for reproducibility.
SRCREV_godot = "e6522ac5aa6fb6f2c974ac0aa599e47e5e3f4b65"
SRCREV_frt = "f76ef86dc41dde64424e1f9de358a607d0de9009"
SRCREV_FORMAT = "godot_frt"

S = "${WORKDIR}/git"

inherit pkgconfig

COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
COMPATIBLE_MACHINE:armv7 = "(.*)"
COMPATIBLE_MACHINE:armv7a = "(.*)"
COMPATIBLE_MACHINE:armv7ve = "(.*)"

GODOT3_ARCH:aarch64 = "arm64"
GODOT3_ARCH:armv7 = "arm32"
GODOT3_ARCH:armv7a = "arm32"
GODOT3_ARCH:armv7ve = "arm32"

GODOT3_BITS = "32"
GODOT3_BITS:aarch64 = "64"

do_compile() {
    cd ${S}
    scons platform=frt target=release_debug tools=no \
        arch=${GODOT3_ARCH} bits=${GODOT3_BITS} \
        use_static_cpp=no optimize=speed progress=yes \
        num_jobs=${BB_NUMBER_THREADS} \
        CC="${CC}" CFLAGS="${CFLAGS}" \
        CXX="${CXX}" CXXFLAGS="${CXXFLAGS}" \
        LINK="${CXX}" LINKFLAGS="${LDFLAGS}" \
        AR="${AR}" RANLIB="${RANLIB}" \
        pkg_config="${STAGING_BINDIR_NATIVE}/pkg-config"
}

do_install() {
    runtime="$(find ${S}/bin -maxdepth 1 -type f -name 'godot.frt*' | head -n 1)"
    if [ -z "$runtime" ]; then
        bbfatal "Godot 3 FRT runtime was not produced"
    fi

    install -d ${D}${bindir}
    install -m 0755 "$runtime" ${D}${bindir}/godot3
}

RDEPENDS:${PN} = "libsdl2"

INSANE_SKIP:${PN} = "already-stripped"
