/**
 * @author Trung Kien Huynh
 */
export const TIMESTAMP_TYPES = Object.freeze({
  MESSAGE: "MESSAGE",
  ITEM: "ITEM",
  USER: "USER",
});

export function formatTimestamp(dateString, type) {
  const date = new Date(dateString);
  const today = new Date();
  const yesterday = new Date(today);
  yesterday.setDate(today.getDate() - 1);

  const time = date.toLocaleTimeString("de-DE", {
    hour: "2-digit",
    minute: "2-digit",
  });
  const dayMonth = date.toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "2-digit",
  });
  const dayMonthYear = date.toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });

  if (type === TIMESTAMP_TYPES.USER) {
    return `${dayMonthYear}`;
  }

  // Heute
  if (date.toDateString() === today.toDateString()) {
    return time;
  }

  // Gestern
  if (date.toDateString() === yesterday.toDateString()) {
    return `Gestern, ${time}`;
  }

  // Gleiches Jahr
  if (date.getFullYear() === today.getFullYear()) {
    switch (type) {
      case TIMESTAMP_TYPES.MESSAGE:
        return `${dayMonth} UM ${time}`;
      case TIMESTAMP_TYPES.ITEM:
        return `${dayMonthYear}`;
    }
  }

  // Anderes Jahr
  switch (type) {
    case TIMESTAMP_TYPES.MESSAGE:
      return `${dayMonthYear} UM ${time}`;
    case TIMESTAMP_TYPES.ITEM:
      return `${dayMonthYear}`;
  }
}
