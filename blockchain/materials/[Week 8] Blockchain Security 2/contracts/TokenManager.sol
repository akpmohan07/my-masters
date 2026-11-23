// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract TokenManager {
    address public admin;
    mapping(address => uint256) public balances;
    constructor() { admin = msg.sender; }

    function mint(address to, uint256 amount) external {
        // accidentally unrestricted mint
        balances[to] += amount;
    }
}
