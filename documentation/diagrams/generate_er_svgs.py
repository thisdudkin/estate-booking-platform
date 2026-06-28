from html import escape
from pathlib import Path

OUT = Path(__file__).resolve().parents[1] / "images"

COLORS = {
    "profile": ("#0f766e", "#ecfeff"),
    "listing": ("#2563eb", "#eff6ff"),
    "media": ("#7c3aed", "#f5f3ff"),
    "search": ("#ca8a04", "#fefce8"),
    "rental": ("#dc2626", "#fef2f2"),
    "moderation": ("#475569", "#f8fafc"),
}


def table(name, x, y, fields, accent, key_fields=()):
    width = max(260, max(len(f"{n} {t}") for n, t in fields) * 7 + 84)
    row_h = 28
    header_h = 44
    height = header_h + row_h * len(fields) + 14
    return {
        "name": name,
        "x": x,
        "y": y,
        "w": width,
        "h": height,
        "fields": fields,
        "accent": accent,
        "keys": set(key_fields),
    }


def center(t):
    return t["x"] + t["w"] / 2, t["y"] + t["h"] / 2


def edge_path(a, b):
    ax, ay = center(a)
    bx, by = center(b)
    if abs(ax - bx) > abs(ay - by):
        start = (a["x"] + (a["w"] if bx > ax else 0), ay)
        end = (b["x"] + (0 if bx > ax else b["w"]), by)
        mid = (start[0] + end[0]) / 2
        return f"M {start[0]} {start[1]} C {mid} {start[1]}, {mid} {end[1]}, {end[0]} {end[1]}"
    start = (ax, a["y"] + (a["h"] if by > ay else 0))
    end = (bx, b["y"] + (0 if by > ay else b["h"]))
    mid = (start[1] + end[1]) / 2
    return f"M {start[0]} {start[1]} C {start[0]} {mid}, {end[0]} {mid}, {end[0]} {end[1]}"


def render(filename, title, palette, tables, links):
    accent, tint = COLORS[palette]
    max_x = max(t["x"] + t["w"] for t in tables.values()) + 48
    max_y = max(t["y"] + t["h"] for t in tables.values()) + 48
    svg = [
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{max_x}" height="{max_y}" viewBox="0 0 {max_x} {max_y}" role="img" aria-labelledby="title desc">',
        f"<title>{escape(title)}</title>",
        f"<desc>Entity relationship diagram for {escape(title)}.</desc>",
        "<defs>",
        '<filter id="shadow" x="-8%" y="-8%" width="116%" height="124%"><feDropShadow dx="0" dy="8" stdDeviation="8" flood-color="#0f172a" flood-opacity=".10"/></filter>',
        '<marker id="arrow" markerWidth="12" markerHeight="12" refX="10" refY="6" orient="auto"><path d="M2,2 L10,6 L2,10" fill="none" stroke="#64748b" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/></marker>',
        "</defs>",
        '<rect width="100%" height="100%" fill="#ffffff"/>',
        f'<rect x="18" y="18" width="{max_x - 36}" height="{max_y - 36}" rx="18" fill="#f8fafc" stroke="#e2e8f0"/>',
        f'<text x="40" y="56" font-family="Inter, Segoe UI, Arial, sans-serif" font-size="22" font-weight="700" fill="#0f172a">{escape(title)}</text>',
    ]
    for source, target, label in links:
        svg.append(f'<path d="{edge_path(tables[source], tables[target])}" fill="none" stroke="#64748b" stroke-width="2.2" marker-end="url(#arrow)"/>')
        if label:
            sx, sy = center(tables[source])
            tx, ty = center(tables[target])
            svg.append(f'<text x="{(sx + tx) / 2}" y="{(sy + ty) / 2 - 8}" text-anchor="middle" font-family="Inter, Segoe UI, Arial, sans-serif" font-size="12" font-weight="700" fill="#475569">{escape(label)}</text>')
    for t in tables.values():
        svg.append(f'<g filter="url(#shadow)">')
        svg.append(f'<rect x="{t["x"]}" y="{t["y"]}" width="{t["w"]}" height="{t["h"]}" rx="10" fill="#ffffff" stroke="#cbd5e1"/>')
        svg.append(f'<rect x="{t["x"]}" y="{t["y"]}" width="{t["w"]}" height="44" rx="10" fill="{accent}"/>')
        svg.append(f'<path d="M {t["x"]} {t["y"] + 34} H {t["x"] + t["w"]} V {t["y"] + 44} H {t["x"]} Z" fill="{accent}"/>')
        svg.append(f'<text x="{t["x"] + 18}" y="{t["y"] + 29}" font-family="Inter, Segoe UI, Arial, sans-serif" font-size="16" font-weight="700" fill="#ffffff">{escape(t["name"])}</text>')
        y = t["y"] + 66
        for field, typ in t["fields"]:
            key = field in t["keys"]
            svg.append(f'<rect x="{t["x"] + 14}" y="{y - 18}" width="{t["w"] - 28}" height="24" rx="6" fill="{tint if key else "#ffffff"}"/>')
            badge = "PK" if key else "FK" if field.endswith("_id") or field == "listing_id" else ""
            if badge:
                svg.append(f'<text x="{t["x"] + 24}" y="{y}" font-family="Inter, Segoe UI, Arial, sans-serif" font-size="10" font-weight="800" fill="{accent}">{badge}</text>')
            svg.append(f'<text x="{t["x"] + 56}" y="{y}" font-family="Inter, Segoe UI, Arial, sans-serif" font-size="13" font-weight="600" fill="#0f172a">{escape(field)}</text>')
            svg.append(f'<text x="{t["x"] + t["w"] - 18}" y="{y}" text-anchor="end" font-family="Inter, Segoe UI, Arial, sans-serif" font-size="12" fill="#64748b">{escape(typ)}</text>')
            y += 28
        svg.append("</g>")
    svg.append("</svg>\n")
    (OUT / filename).write_text("\n".join(svg), encoding="utf-8")


def fields(*items):
    return [tuple(item.split(" : ", 1)) for item in items]


render("profiles_db_schema.svg", "Profile Service Database Schema", "profile", {
    "user_profiles": table("user_profiles", 420, 90, fields("id : uuid", "keycloak_user_id : uuid", "email : varchar(255)", "display_name : varchar(160)", "phone : varchar(32)", "status : varchar(32)", "created_at : timestamptz", "updated_at : timestamptz"), "profile", ["id"]),
    "tenant_profiles": table("tenant_profiles", 80, 430, fields("profile_id : uuid", "preferred_city : varchar(120)", "preferences : jsonb", "created_at : timestamptz", "updated_at : timestamptz"), "profile", ["profile_id"]),
    "landlord_profiles": table("landlord_profiles", 780, 430, fields("profile_id : uuid", "display_company_name : varchar(180)", "verification_status : varchar(32)", "business_metadata : jsonb", "created_at : timestamptz", "updated_at : timestamptz"), "profile", ["profile_id"]),
    "favorite_listings": table("favorite_listings", 105, 760, fields("tenant_profile_id : uuid", "listing_id : uuid", "created_at : timestamptz"), "profile", ["tenant_profile_id", "listing_id"]),
    "processed_identity_events": table("processed_identity_events", 760, 120, fields("event_id : uuid", "event_type : varchar(80)", "keycloak_user_id : uuid", "processed_at : timestamptz"), "profile", ["event_id"]),
}, [("user_profiles", "tenant_profiles", "1:1"), ("user_profiles", "landlord_profiles", "1:1"), ("tenant_profiles", "favorite_listings", "1:N")])

render("listing_db_schema.svg", "Listing Service Database Schema", "listing", {
    "listings": table("listings", 330, 90, fields("id : uuid", "landlord_profile_id : uuid", "title : varchar(200)", "description : text", "property_type : varchar(40)", "deal_type : varchar(40)", "price_amount : numeric(12,2)", "price_currency : char(3)", "status : varchar(32)", "version : bigint", "created_at : timestamptz", "updated_at : timestamptz"), "listing", ["id"]),
    "listing_locations": table("listing_locations", 80, 560, fields("listing_id : uuid", "country : varchar(80)", "city : varchar(120)", "district : varchar(120)", "street : varchar(180)", "building : varchar(40)", "latitude : numeric(10,7)", "longitude : numeric(10,7)"), "listing", ["listing_id"]),
    "listing_details": table("listing_details", 680, 610, fields("listing_id : uuid", "rooms : integer", "total_area : numeric(8,2)", "floor : integer", "floors_total : integer", "attributes : jsonb"), "listing", ["listing_id"]),
}, [("listings", "listing_locations", "1:1"), ("listings", "listing_details", "1:1")])

render("media_db_schema.svg", "Media Service Database Schema", "media", {
    "media_assets": table("media_assets", 95, 90, fields("media_id : uuid", "listing_id : uuid", "owner_profile_id : uuid", "bucket : varchar", "storage_key : varchar", "content_type : varchar", "size_bytes : bigint", "checksum : varchar", "status : varchar", "sort_order : integer", "created_at : timestamp", "updated_at : timestamp"), "media", ["media_id"]),
    "media_processing_jobs": table("media_processing_jobs", 95, 560, fields("job_id : uuid", "media_id : uuid", "job_type : varchar", "status : varchar", "error_message : text", "created_at : timestamp", "updated_at : timestamp"), "media", ["job_id"]),
}, [("media_assets", "media_processing_jobs", "1:N")])

render("search_db_schema.svg", "Search Service Database Schema", "search", {
    "listing_search_documents": table("listing_search_documents", 80, 90, fields("listing_id : uuid", "listing_version : bigint", "title : text", "city : keyword", "district : keyword", "location : geo_point", "property_type : keyword", "deal_type : keyword", "price_amount : decimal", "price_currency : keyword", "rooms : integer", "total_area : decimal", "media : nested", "status : keyword", "published_at : timestamp", "updated_at : timestamp"), "search", ["listing_id"]),
    "saved_searches": table("saved_searches", 560, 150, fields("saved_search_id : uuid", "tenant_profile_id : uuid", "name : text", "filters : json", "created_at : timestamp"), "search", ["saved_search_id"]),
    "projection_offsets": table("projection_offsets", 560, 440, fields("topic_partition : varchar", "topic : varchar", "partition : integer", "offset : bigint", "updated_at : timestamp"), "search", ["topic_partition"]),
}, [])

render("rental_request_db_schema.svg", "Rental Request Service Database Schema", "rental", {
    "rental_requests": table("rental_requests", 285, 90, fields("id : uuid", "listing_id : uuid", "tenant_profile_id : uuid", "landlord_profile_id : uuid", "status : varchar(32)", "initial_message : text", "version : bigint", "created_at : timestamptz", "updated_at : timestamptz"), "rental", ["id"]),
    "request_messages": table("request_messages", 80, 470, fields("id : uuid", "request_id : uuid", "sender_profile_id : uuid", "body : text", "created_at : timestamptz"), "rental", ["id"]),
    "viewings": table("viewings", 620, 470, fields("id : uuid", "request_id : uuid", "starts_at : timestamptz", "ends_at : timestamptz", "status : varchar(32)", "created_at : timestamptz", "updated_at : timestamptz"), "rental", ["id"]),
}, [("rental_requests", "request_messages", "1:N"), ("rental_requests", "viewings", "1:N")])

render("moderation_db_schema.svg", "Moderation Service Database Schema", "moderation", {
    "moderation_cases": table("moderation_cases", 285, 90, fields("id : uuid", "listing_id : uuid", "landlord_profile_id : uuid", "status : varchar(32)", "priority : varchar(24)", "assigned_moderator_id : uuid", "created_at : timestamptz", "updated_at : timestamptz"), "moderation", ["id"]),
    "moderation_decisions": table("moderation_decisions", 75, 450, fields("id : uuid", "case_id : uuid", "moderator_profile_id : uuid", "decision : varchar(32)", "reason : text", "created_at : timestamptz"), "moderation", ["id"]),
    "complaints": table("complaints", 620, 450, fields("id : uuid", "listing_id : uuid", "reporter_profile_id : uuid", "reason : varchar(80)", "description : text", "status : varchar(32)", "created_at : timestamptz", "updated_at : timestamptz"), "moderation", ["id"]),
}, [("moderation_cases", "moderation_decisions", "1:N")])
