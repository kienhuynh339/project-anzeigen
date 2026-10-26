/**
 * @author Trung Kien Huynh
 */
export const truncateText = (text, limiter) => {
  return text.length > limiter ? text.slice(0, limiter) + ".." : text;
};
