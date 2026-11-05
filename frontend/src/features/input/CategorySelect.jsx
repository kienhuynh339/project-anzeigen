/**
 * @author Trung Kien Huynh
 */
import { getPath, useFetch } from "../../hooks/useFetch";

export default function CategorySelect({ categoryId, setCategoryId }) {
  const { data: categories } = useFetch(
    getPath.categories(),
    [],
    [{ id: 1, name: "" }],
  );
  return (
    <select
      name="category"
      className="form-select white-focus"
      value={categoryId}
      onChange={(e) => {
        setCategoryId(e.target.value);
      }}
    >
      {categories.map((category) => (
        <option key={category.id} value={category.id}>
          {category.name}
        </option>
      ))}
    </select>
  );
}
