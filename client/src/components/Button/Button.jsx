import './Button.css';

const Button = ({
  children,
  variant = 'confirm', // gree, blue, red, orange, purple
  size = 'medium',     // small, medium, large
  disabled = false,
  onClick,
  type = 'button',
  className = '',
  ...props
}) => {
  return (
    <button
      type={type}
      disabled={disabled}
      onClick={onClick}
      className={`btn btn-${variant} btn-${size} ${disabled ? 'btn-disabled' : ''} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
};

export default Button;