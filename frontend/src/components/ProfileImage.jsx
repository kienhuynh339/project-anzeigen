const profileImages = Object.fromEntries(
  Object.entries(
    import.meta.glob("../assets/profile_images/*.svg", {
      eager: true,
      import: "default",
    }),
  ).map(([path, image]) => {
    const name = path.split("/").pop().replace(".svg", "");
    return [name.toLowerCase(), image];
  }),
);

const normalizeGerman = (str) =>
  str
    .replace("ä", "ae")
    .replace("ö", "oe")
    .replace("ü", "ue")
    .replace("ß", "ss");

export default function ProfileImage({
  firstLetterOfName,
  size = "30",
  backgroundColor = "#ABABAB",
}) {
  return (
    <div
      className="rounded-circle"
      style={{ backgroundColor: backgroundColor }}
    >
      <img
        src={profileImages[normalizeGerman(firstLetterOfName.toLowerCase())]}
        width={size}
        height={size}
        style={{ objectFit: "cover" }}
      />
    </div>
  );
}
