/**
 * @author Trung Kien Huynh
 */
export function getImage(imageId) {
  return fetch(`http://localhost:8080/api/images/${imageId}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`Bild ${imageId} konnte nicht geladen werden.`);
      }
      return response.blob();
    })
    .then((blob) => {
      // new File(fileBits, fileName, options). BLOB (Binary Large Object): Binärdaten
      return new File([blob], `image-${imageId}`, { type: blob.type });
    });
}
