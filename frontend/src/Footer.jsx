function Footer() {
  return (
    <footer className="bg-success-subtle mt-auto py-3 border border-dark-subtle flex-grow-1">
      <div className="container">
        <div className="text-center text-secondary">
          <small>
            &copy; {new Date().getFullYear()} anzeigen. Alle Rechte vorbehalten.
          </small>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
