(function () {
  function findInShadows(target, shadowRoots, searchContext) {
    if (shadowRoots.length === 0) {
      return Array.from(searchContext.querySelectorAll(target));
    }

    var nextSelector = shadowRoots[0];
    var otherSelectors = shadowRoots.slice(1);
    var nextInnerShadowRoots = Array.from(searchContext.querySelectorAll(nextSelector));
    return nextInnerShadowRoots.map(function (childShadowRoot) {
      return findInShadows(target, otherSelectors, getShadowRoot(childShadowRoot));
    }).flat();
  }

  function getShadowRoot(element) {
    if (!element.shadowRoot) {
      throw Error('The element is not a shadow host or has \'closed\' shadow-dom mode: ' + element);
    }
    return element.shadowRoot;
  }

  function searchContext(element) {
    return !element ? document : (element.shadowRoot || element);
  }

  return findInShadows(arguments[0], arguments[1], searchContext(arguments[2]));
})(...arguments)
