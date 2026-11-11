/**
 * @author Trung Kien Huynh
 */

// enthält {Dateiname(ohne Endung):PfadZurDatei,...}
const PROFILE_IMAGES = Object.fromEntries(
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
      className="rounded-circle "
      style={{
        backgroundColor: backgroundColor,
        width: `${size}px`,
        height: `${size}px`,
      }}
    >
      <img
        src={PROFILE_IMAGES[normalizeGerman(firstLetterOfName.toLowerCase())]}
        className="rounded-circle"
        width={size}
        height={size}
        style={{ objectFit: "cover" }}
      />
    </div>
  );
}
