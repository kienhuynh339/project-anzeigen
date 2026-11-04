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
export default function ProfileImage({
  firstLetterOfName,
  backgroundColor = "ABABAB",
}) {
  return (
    <div className="">
      <img src={profileImages[firstLetterOfName]} />
    </div>
  );
}
