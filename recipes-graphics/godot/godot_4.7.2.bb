# Keep packaging and cross-compilation policy aligned with the existing recipe,
# while tracking the current supported Godot stable release.
require godot_4.3.bb

SRCREV = "ed1daf0bf001b61586d9930840f2f1394092c079"
SRC_URI = " \
    git://github.com/godotengine/godot.git;protocol=https;lfs=0;branch=4.7 \
    file://0002-enable-clang.patch \
    file://0003-egl-select-api-compatible-window-config.patch \
"
