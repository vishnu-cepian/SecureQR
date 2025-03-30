pragma solidity ^0.8.0;
// SPDX-License-Identifier: MIT
contract SafeBusinessQR {

    mapping(string => mapping(bytes32 => bool)) public registeredProducts;

    function registerProduct(string memory company, bytes32 qrHash) public {
        require(!registeredProducts[company][qrHash], "Product is already registered!");
        registeredProducts[company][qrHash] = true;
    }


    function isProductAuthentic(string memory company, bytes32 qrHash) public view returns (bool) {
        return registeredProducts[company][qrHash];
    }
}
