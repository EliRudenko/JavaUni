def parse_ini(path):
    result = {}

    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()

            if not line:
                continue

            if line.startswith("#") or line.startswith(";"):
                continue

            comment_pos = None
            for c in ("#", ";"):
                pos = line.find(c)
                if pos != -1 and (comment_pos is None or pos < comment_pos):
                    comment_pos = pos

            if comment_pos is not None:
                line = line[:comment_pos].strip()

            if not line:
                continue

            if ":" in line:
                key, value = line.split(":", 1)
                result[key.strip()] = value.strip()

    return result
