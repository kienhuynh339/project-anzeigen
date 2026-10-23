export const TimestampType = Object.freeze({
  MESSAGE: "MESSAGE",
  ITEM: "ITEM",
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

  // Heute
  if (date.toDateString() === today.toDateString()) {
    return time;
  }

  // Gestern
  if (date.toDateString() === yesterday.toDateString()) {
    return `Gestern, ${time}`;
  }

  // Datum und Monat
  const dayMonth = date.toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "2-digit",
  });

  const dayMonthYear = date.toLocaleDateString("de-DE", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });

  // Gleiches Jahr
  if (date.getFullYear() === today.getFullYear()) {
    switch (type) {
      case TimestampType.MESSAGE:
        return `${dayMonth} UM ${time}`;
      case TimestampType.ITEM:
        return `${dayMonthYear}`;
    }
  }

  // Anderes Jahr
  switch (type) {
    case TimestampType.MESSAGE:
      return `${dayMonthYear} UM ${time}`;
    case TimestampType.ITEM:
      return `${dayMonthYear}`;
  }
}
