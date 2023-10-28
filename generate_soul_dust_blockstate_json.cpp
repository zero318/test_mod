// Type your code here, or load an example.
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <algorithm>
//#include <math.h>
#include <vector>

#define unreachable __builtin_unreachable()
#define assume(condition) __builtin_assume(condition)

struct Color {
    int r;
    int g;
    int b;
};

enum Direction {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST
};

Direction get_opposite_direction(Direction in) {
    return (Direction)(in ^ 1);
    switch (in) {
        default: unreachable;
        case DOWN:  return UP;
        case UP:    return DOWN;
        case NORTH: return SOUTH;
        case SOUTH: return NORTH;
        case WEST:  return EAST;
        case EAST:  return WEST;
    }
}

static Color COLORS[16] = {
    { 15, 69, 71 },
    { 22, 102, 105 },
    { 24, 111, 114 },
    { 26, 120, 123 },
    { 28, 129, 133 },
    { 30, 138, 142 },
    { 32, 147, 151 },
    { 34, 156, 161 },
    { 36, 166, 170 },
    { 38, 175, 179 },
    { 40, 186, 190 },
    { 42, 195, 200 },
    { 44, 204, 209 },
    { 46, 213, 218 },
    { 48, 222, 228 },
    { 50, 231, 237 }
};

enum RedstoneDustSide {
    DUST_NONE = 0,
    DUST_SIDE = 1,
    DUST_UP = 2
};

#define dust_shape_type uint32_t

union RedstoneDustShape {
    dust_shape_type full;
    struct {
        dust_shape_type north : 2;
        dust_shape_type south : 2;
        dust_shape_type west : 2;
        dust_shape_type east : 2;
    };
};

struct RedstoneDust {
    RedstoneDustShape shape;
    uint8_t power;
};

#define NORTH_MASK	(0x03)
#define SOUTH_MASK	(0x0C)
#define WEST_MASK	(0x30)
#define EAST_MASK	(0xC0)

RedstoneDustShape get_connections(RedstoneDustShape dust_shape) {
    /*
    bool has_north = dust_shape.north;
    bool has_east = dust_shape.east;
    bool has_south = dust_shape.south;
    bool has_west = dust_shape.west;
    if (!has_west && !has_north && !has_south) {
        dust_shape.west |= 1;
    }
    if (!has_east && !has_north && !has_south) {
        dust_shape.east |= 1;
    }
    if (!has_north && !has_east && !has_west) {
        dust_shape.north |= 1;
    }
    if (!has_south && !has_east && !has_west) {
        dust_shape.south |= 1;
    }
    return dust_shape;
    */

    RedstoneDustShape ret = dust_shape;
    ret.west |= (bool)(dust_shape.full & ~EAST_MASK);
    ret.east |= (bool)(dust_shape.full & ~WEST_MASK);
    ret.north |= (bool)(dust_shape.full & ~SOUTH_MASK);
    ret.south |= (bool)(dust_shape.full & ~NORTH_MASK);
    return ret;
    
}

RedstoneDustShape get_connections2(RedstoneDustShape dust_shape) {
    RedstoneDustShape ret = dust_shape;
    if (dust_shape.full & ~EAST_MASK) ret.west |= 1;
    if (dust_shape.full & ~WEST_MASK) ret.east |= 1;
    if (dust_shape.full & ~SOUTH_MASK) ret.north |= 1;
    if (dust_shape.full & ~NORTH_MASK) ret.south |= 1;
    return ret;
    
}

RedstoneDustShape set_connection(RedstoneDustShape dust_shape, Direction direction, dust_shape_type new_side) {
    assume(new_side <= 2);
    switch (direction) {
        default: unreachable;
        case NORTH: dust_shape.north = new_side; break;
        case SOUTH: dust_shape.south = new_side; break;
        case WEST: dust_shape.west = new_side; break;
        case EAST: dust_shape.east = new_side; break;
    }
    return dust_shape;
}

RedstoneDustShape mirror(RedstoneDustShape dust_shape, uint8_t mirror_type) {
    switch (mirror_type) {
        default: unreachable;
        case 0:
            break;
        case 1: {
            dust_shape_type temp = dust_shape.north;
            dust_shape.north = dust_shape.south;
            dust_shape.south = temp;
            break;
        }
        case 2: {
            dust_shape_type temp = dust_shape.west;
            dust_shape.west = dust_shape.east;
            dust_shape.east = temp;
            break;
        }
    }
    return dust_shape;
}

#define CLOCKWISE_90 1
#define CLOCKWISE_180 2
#define COUNTERCLOCKWISE_90 3

RedstoneDustShape rotate(RedstoneDustShape dust_shape, uint8_t rotate_type) {
    switch (rotate_type) {
        default: unreachable;
        case 0:
            break;
        case 1: {
            dust_shape_type temp = dust_shape.north;
            dust_shape.north = dust_shape.west;
            dust_shape.west = dust_shape.south;
            dust_shape.south = dust_shape.east;
            dust_shape.east = temp;
            break;
        }
        case 2: {
            dust_shape_type temp = dust_shape.north;
            dust_shape.north = dust_shape.south;
            dust_shape.south = temp;
            temp = dust_shape.west;
            dust_shape.west = dust_shape.east;
            dust_shape.east = temp;
            break;
        }
        case 3: {
            dust_shape_type temp = dust_shape.north;
            dust_shape.north = dust_shape.east;
            dust_shape.east = dust_shape.south;
            dust_shape.south = dust_shape.west;
            dust_shape.west = temp;
            break;
        }
    }
    return dust_shape;
}

bool is_cross(const RedstoneDustShape dust_shape) {
    return dust_shape.north & dust_shape.east & dust_shape.south & dust_shape.west;
}

uint32_t mask_from_ordinal(uint32_t original_value, uint32_t ordinal, uint32_t new_value) {
    uint32_t ordinal_shift = ordinal * 2 - 4;
    uint32_t ordinal_mask = 0x3 << ordinal_shift;
    return (original_value & ~ordinal_mask) | new_value << ordinal_shift;
}

#define NORTH_SIDE		(0x01)
#define NORTH_UP		(0x02)
#define SOUTH_SIDE		(0x04)
#define SOUTH_UP		(0x08)
#define WEST_SIDE		(0x10)
#define WEST_UP			(0x20)
#define EAST_SIDE		(0x40)
#define EAST_UP			(0x80)

#define has_north(i) ((i) & NORTH_MASK)
#define has_south(i) ((i) & SOUTH_MASK)
#define has_west(i) ((i) & WEST_MASK)
#define has_east(i) ((i) & EAST_MASK)


int main() {
    
    size_t total = 0;
    std::vector<size_t> dot_indices;
    std::vector<size_t> north_side_indices;
    std::vector<size_t> south_side_indices;
    std::vector<size_t> west_side_indices;
    std::vector<size_t> east_side_indices;
    std::vector<size_t> north_up_indices;
    std::vector<size_t> south_up_indices;
    std::vector<size_t> west_up_indices;
    std::vector<size_t> east_up_indices;
    for (size_t i = 0; i < 171; ++i) {
        if (__builtin_popcount(i) != 1 &&
            (i & NORTH_MASK) != NORTH_MASK &&
            (i & SOUTH_MASK) != SOUTH_MASK &&
            (i & WEST_MASK) != WEST_MASK &&
            (i & EAST_MASK) != EAST_MASK
        ) {
            ++total;
            printf("%zu, ", i);
            if (!i ||
                (has_north(i) && has_east(i)) ||
                (has_south(i) && has_east(i)) ||
                (has_south(i) && has_west(i)) ||
                (has_north(i) && has_west(i))
            ) {
                dot_indices.push_back(i);
            }
            if (has_north(i)) {
                north_side_indices.push_back(i);
                if (i & NORTH_UP) {
                    north_up_indices.push_back(i);
                }
            }
            if (has_south(i)) {
                south_side_indices.push_back(i);
                if (i & SOUTH_UP) {
                    south_up_indices.push_back(i);
                }
            }
            if (has_west(i)) {
                west_side_indices.push_back(i);
                if (i & WEST_UP) {
                    west_up_indices.push_back(i);
                }
            }
            if (has_east(i)) {
                east_side_indices.push_back(i);
                if (i & EAST_UP) {
                    east_up_indices.push_back(i);
                }
            }
        }
    }

    bool has_preceding_model = false;
    auto render_list = [&](auto& list, const char* model) {
        if (size_t count = list.size()) {
            if (has_preceding_model) putchar(',');
            printf("\n" R"({"when":{"connections":")");
            for (size_t i = 0; i < count;) {
                printf("%zu", list[i]);
                if (++i != count) putchar('|');
            }
            printf(R"("},"apply":{%s}})", model);
            has_preceding_model = true;
        }
    };

    printf(
        "\nModel Stats:\n"
        "Total:\t%zu\n"
        "Dots:\t%zu\n"
        "NS:\t%zu\tNU:%zu\n"
        "SS:\t%zu\tSU:%zu\n"
        "WS:\t%zu\tWU:%zu\n"
        "ES:\t%zu\tEU:%zu\n"
        , total
        , dot_indices.size()
        , north_side_indices.size(), north_up_indices.size()
        , south_side_indices.size(), south_up_indices.size()
        , west_side_indices.size(), west_up_indices.size()
        , east_side_indices.size(), east_up_indices.size()
    );

    printf(
        R"({"multipart":[)"
    );

    render_list(dot_indices, R"("model":"minecraft:block/redstone_dust_dot")");
    render_list(north_side_indices, R"("model":"minecraft:block/redstone_dust_side0")");
    render_list(south_side_indices, R"("model":"minecraft:block/redstone_dust_side_alt0")");
    render_list(west_side_indices, R"("model":"minecraft:block/redstone_dust_side1","y":270)");
    render_list(east_side_indices, R"("model":"minecraft:block/redstone_dust_side_alt1","y":270)");
    render_list(north_up_indices, R"("model":"minecraft:block/redstone_dust_up")");
    render_list(south_up_indices, R"("model":"minecraft:block/redstone_dust_up","y":180)");
    render_list(west_up_indices, R"("model":"minecraft:block/redstone_dust_up","y":270)");
    render_list(east_up_indices, R"("model":"minecraft:block/redstone_dust_up","y":90)");
    
    /*
    if (size_t count = dot_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", dot_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_dot\"}}");
        has_preceding_model = true;
    }
    if (size_t count = north_side_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", north_side_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_side0\"}}");
        has_preceding_model = true;
    }
    if (size_t count = south_side_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", south_side_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_side_alt0\"}}");
        has_preceding_model = true;
    }
    if (size_t count = west_side_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", west_side_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_side1\",\"y\":270}}");
        has_preceding_model = true;
    }
    if (size_t count = east_side_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", east_side_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_side_alt1\",\"y\":270}}");
        has_preceding_model = true;
    }
    if (size_t count = north_up_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", north_up_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_up\"}}");
        has_preceding_model = true;
    }
    if (size_t count = south_up_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", south_up_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_up\",\"y\":180}}");
        has_preceding_model = true;
    }
    if (size_t count = west_up_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", west_up_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_up\",\"y\":270}}");
        has_preceding_model = true;
    }
    if (size_t count = east_up_indices.size()) {
        if (has_preceding_model) putchar(',');
        printf("\n{\"when\":{\"OR\":[");
        for (size_t i = 0; i < count;) {
            printf("{\"connections\":%zu}", east_up_indices[i]);
            if (++i != count) putchar(',');
        }
        printf("]},\"apply\":{\"model\":\"minecraft:block/redstone_dust_up\",\"y\":90}}");
        has_preceding_model = true;
    }
    */
    printf(
        "\n]}"
    );

    return 0;
}