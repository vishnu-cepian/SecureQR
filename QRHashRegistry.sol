pragma solidity ^0.8.0;
// SPDX-License-Identifier: MIT
contract QRHashRegistry {

    mapping(bytes32 => bool) public MaliciousHashes;

    // Add a new hash to the blockchain
    function addQRHash(bytes32 _qrHash) public {
        require(!MaliciousHashes[_qrHash], "Hash already exists");
        MaliciousHashes[_qrHash] = true;
    }

    // Check if a hash is malicious
    function isHashMalicious(bytes32 _qrHash) public view returns (bool) {
        return MaliciousHashes[_qrHash];
    }
}
