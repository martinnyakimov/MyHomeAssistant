export function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

export function isEmpty(string) {
  return string == null || string == '';
}

export function trim(string) {
  return string.replace(/^\s+|\s+$/gm, '');
}

export function formatDate(date) {
  const monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June', 'July', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec'];

  let day = date.getDate();
  let monthIndex = date.getMonth();
  let year = date.getFullYear();
  let hour = date.getHours();
  let minutes = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();

  return `${day} ${monthNames[monthIndex]} ${year} - ${hour}:${minutes}`;
}
