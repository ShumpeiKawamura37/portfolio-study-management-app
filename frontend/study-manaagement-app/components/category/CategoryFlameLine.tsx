"use client";

const INDENT_WIDTH = 24;

type Props = {
  ancestorHasLine: boolean[];
  isLast: boolean;
};

export default function CategoryLine({
  ancestorHasLine,
  isLast,
}: Props) {
  const width = (ancestorHasLine.length + 1) * INDENT_WIDTH;

  return (
    <div
      className="relative shrink-0 h-8"
      style={{ width }}
    >
      {/* 祖先の縦線 */}
      {ancestorHasLine.map((hasLine, index) =>
        hasLine ? (
          <div
            key={index}
            className="absolute top-0 bottom-0 w-px bg-gray-400"
            style={{
              left: index * INDENT_WIDTH + INDENT_WIDTH / 2,
            }}
          />
        ) : null
      )}

      {/* 自分の縦線 */}
      <div
        className={`absolute w-px bg-gray-400 ${
          isLast ? "h-4" : "top-0 bottom-0"
        }`}
        style={{
          left:
            ancestorHasLine.length * INDENT_WIDTH +
            INDENT_WIDTH / 2,
        }}
      />

      {/* 横線 */}
      <div
        className="absolute h-px w-3 bg-gray-400"
        style={{
          top: 16,
          left:
            ancestorHasLine.length * INDENT_WIDTH +
            INDENT_WIDTH / 2,
        }}
      />
    </div>
  );
}